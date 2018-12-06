#include<iostream>
#include<stdio.h>
#include<string.h>
#include<fstream>

using namespace std;


void stringToByte(char *p) {
    int len  = strlen(p);
    int acc = 0;
    for(int i=0;i<len;i+=2) { char c1 = p[i];
        char c2 = p[i+1];
        if(c1>'a') c1 = c1 - 'a' + 'A';
        if(c2>'a') c2 = c2 - 'a' + 'A';
        if(i==len-2) printf("0x%01c%01c",c1,c2);
        else
            printf("0x%01c%01c,",c1,c2);
    }
    printf("\n");
}

void byteToString(string buf) {
    int startpos = 0;
    while((startpos=buf.find('x',startpos)) != -1) {
        printf("%s",buf.substr(startpos+1,2).c_str());
        startpos = buf.find('x',startpos+1);
    }
    printf("\n");
    /*
    int len = strlen(p);
    int acc = 0;
    while(len>0) {
        printf("%.2x",*p);
        p++;
        acc++;
        len--;
        if(acc!=0 && acc%32 == 0 ) printf("\n");
    }
    printf("\n");
    */
}

int main(int argc, char *argv[]) {
    if(argc == 3) {
        char *fileName=argv[2];
        if(strcmp(argv[1],"byte") == 0) {
            ifstream is(fileName);
            char buf[128];
            while(is.getline(buf,128)) {
                stringToByte(buf);
            }
        } else if(strcmp(argv[1],"string") == 0) {
            ifstream is(fileName);
            string tmp;
            while(getline(is,tmp)) {
                byteToString(tmp);
            }
        }
    }
    else {
        printf("[ERROR] Please input output format(byte/string) and file path!\n");
        printf("Usage: ./bsSwitcher <type> <file>\n");
        printf("       --type   'string' or 'byte'\n");
        printf("       --file   file path\n");
    }
    return 1;
}
