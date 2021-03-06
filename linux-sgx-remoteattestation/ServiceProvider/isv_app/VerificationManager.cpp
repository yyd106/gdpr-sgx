#include "VerificationManager.h"
#include "../GeneralSettings.h"

#include  <iomanip>

using namespace util;
using namespace std;

VerificationManager* VerificationManager::instance = NULL;

VerificationManager::VerificationManager() {
    this->nm = NetworkManagerClient::getInstance(Settings::rh_port, Settings::rh_host);
    this->ws = WebService::getInstance();
    this->ws->init();
    this->sp = new ServiceProvider(this->ws);
}


VerificationManager::~VerificationManager() {}


VerificationManager* VerificationManager::getInstance() {
    if (instance == NULL) {
        instance = new VerificationManager();
    }

    return instance;
}


int VerificationManager::init(int isVerify) {
    this->isVerify = isVerify;
    if (this->sp) {
        delete this->sp;
        this->sp = new ServiceProvider(this->ws);
    }
    this->sp->setIsVerify(isVerify);

    this->nm->Init();
    this->nm->connectCallbackHandler([this](string v, int type) {
        return this->incomingHandler(v, type);
    });
}


void VerificationManager::start() {
    this->nm->startService();
    Log("Remote attestation done");
}


string VerificationManager::handleMSG0(Messages::MessageMsg0 msg) {
    Log("MSG0 received");

    if (msg.status() != TYPE_TERMINATE) {
        uint32_t extended_epid_group_id = msg.epid();
        int ret = this->sp->sp_ra_proc_msg0_req(extended_epid_group_id);

        if (ret == 0) {
            msg.set_status(TYPE_OK);
            return nm->serialize(msg);
        }
    } else {
        Log("Termination received!");
    }
    
    return "";    
}


string VerificationManager::handleMSG1(Messages::MessageMSG1 msg1) {
    Log("MSG1 received");

    Messages::MessageMSG2 msg2;
    msg2.set_type(Messages::Type::RA_MSG2);

    int ret = this->sp->sp_ra_proc_msg1_req(msg1, &msg2);

    if (ret != 0) {
        Log("Error, processing MSG1 failed");
        cout<<"Error num:"<<ret<<endl;
    } else {
        Log("MSG1 processed correctly and MSG2 created");
        return nm->serialize(msg2);
    }

    return "";
}


string VerificationManager::handleMSG3(Messages::MessageMSG3 msg) {
    Log("MSG3 received");

    Messages::AttestationMessage att_msg;
    att_msg.set_type(Messages::Type::RA_ATT_RESULT);

    int ret = this->sp->sp_ra_proc_msg3_req(msg, &att_msg);

    if (ret == -1) {
        Log("Error, processing MSG3 failed");
    } else {
        Log("MSG3 processed correctly and attestation result created");
        return nm->serialize(att_msg);
    }

    return "";
}


string VerificationManager::handleAppAttOk() {
    Log("APP attestation result received");
    return "";
}


string VerificationManager::prepareVerificationRequest() {
    Log("Prepare Verification request");

    Messages::InitialMessage msg;
    msg.set_type(Messages::Type::RA_VERIFICATION);

    return nm->serialize(msg);
}


string VerificationManager::createInitMsg(int type, string msg) {
    Messages::InitialMessage init_msg;
    init_msg.set_type(type);
    init_msg.set_size(msg.size());

    return nm->serialize(init_msg);
}
/*
*/


vector<string> VerificationManager::incomingHandler(string v, int type) {
    vector<string> res;

    if (!v.empty()) {
        string s;
        bool ret;

        switch (type) {
        case Messages::Type::RA_MSG0: {
            Messages::MessageMsg0 msg0;
            ret = msg0.ParseFromString(v);
            if (ret && (msg0.type() == Messages::Type::RA_MSG0)) {
                s = this->handleMSG0(msg0);
                res.push_back(to_string(Messages::Type::RA_MSG0));
            }
        }
        break;
        case Messages::Type::RA_MSG1: {
            Messages::MessageMSG1 msg1;
            ret = msg1.ParseFromString(v);
            if (ret && (msg1.type() == Messages::Type::RA_MSG1)) {
                // generate MSG2 and send to enclave
                s = this->handleMSG1(msg1);
                res.push_back(to_string(Messages::Type::RA_MSG2));
            }
        }
        break;
        case Messages::Type::RA_MSG3: {
            Messages::MessageMSG3 msg3;
            ret = msg3.ParseFromString(v);
            if (ret && (msg3.type() == Messages::Type::RA_MSG3)) {
                // generate MSG4 and send to enclave
                s = this->handleMSG3(msg3);
                res.push_back(to_string(Messages::Type::RA_ATT_RESULT));
            }
        }
        break;
        case Messages::Type::RA_APP_ATT_OK: {   // recieve ACK from enclave
            Messages::SecretMessage sec_msg;
            ret = sec_msg.ParseFromString(v);
            if (ret) {
                if (sec_msg.type() == Messages::Type::RA_APP_ATT_OK) {
                    this->handleAppAttOk();
                    Log("========== send a secret to app ==========");
                    res.push_back(to_string(SGX_SEAL_SECRET));
                    Messages::SecretMessage secret_msg;
                    secret_msg.set_type(SGX_SEAL_SECRET);
                    secret_msg.set_size(60);
                    s = this->nm->serialize(secret_msg);
                }
            }
        }
        break;
        default:
            Log("Unknown type: %d", type, log::error);
            break;
        }

        res.push_back(s);
    } else { 	//after handshake
        res.push_back(to_string(Messages::Type::RA_VERIFICATION));
        res.push_back(this->prepareVerificationRequest());
    }

    return res;
}
