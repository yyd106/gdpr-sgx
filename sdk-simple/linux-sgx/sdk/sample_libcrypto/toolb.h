#if !defined _TOOLB_H_
#define _TOOLB_H_
#include "ippcp.h"
#include <stdlib.h>
Ipp32u* randIpp32u(Ipp32u* X, int size);
IppsBigNumState* nBN(int dS, const Ipp32u* A=0);
IppsBigNumState* nBNneg(int dS, const Ipp32u* A=0);
IppsPRNGState* nPRNG(int seedBitsize=160);
IppsPrimeState* nPrimG(int ,IppsPRNGState*);
//IppsRSAState* nRSA(int NbitS, int PbitS, IppRSAKeyType type);
IppsDLPState* nDLP(int PbitS, int QbitS);
IppsECCPState* nECCP(int bitS);
//IppsECCBState* nECCB(int bitS);
IppsECCPPointState* nECCPPoint(int bitS,int Psize);
//IppsECCBPointState* nECCBPoint(int bitS,int Psize);
void tBN(const char* Msg, const IppsBigNumState* BN);
#endif // _TOOLB_H_
