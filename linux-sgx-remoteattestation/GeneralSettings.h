#ifndef GENERALSETTINGS_H
#define GENERALSETTINGS_H

#include <string>

using namespace std;

namespace Settings {
	static int rh_port = 22222;
	static string rh_host = "localhost";
	
	static string server_crt = "/home/vdeadmin/errands/linux-sgx-remoteattestation/cert/tmp/server.crt"; //certificate for the HTTPS connection between the SP and the App
	static string server_key = "/home/vdeadmin/errands/linux-sgx-remoteattestation/cert/tmp/server.key"; //private key for the HTTPS connection

	static string spid = "4F2229E41A0ECA12BAE6EDB36E87EAFE"; //SPID provided by Intel after registration for the IAS service
	static const char *ias_crt = "/home/vdeadmin/errands/linux-sgx-remoteattestation/cert/tmp/server.crt"; //location of the certificate send to Intel when registring for the IAS
	static string ias_url = "https://test-as.sgx.trustedservices.intel.com:443/attestation/sgx/v1/";
    static const char *ec_pri_key_path = "/home/vdeadmin/errands/linux-sgx-remoteattestation/Application/ec256_private_key";
    static const char *ec_pub_key_path = "/home/vdeadmin/errands/linux-sgx-remoteattestation/Application/ec256_public_key";
    static const char *ec_pub_key_path_server = "/home/vdeadmin/errands/linux-sgx-remoteattestation/ServiceProvider/ec256_public_key";
    static const char *ec_pri_key_path_server = "/home/vdeadmin/errands/linux-sgx-remoteattestation/ServiceProvider/ec256_private_key";
}

#endif
