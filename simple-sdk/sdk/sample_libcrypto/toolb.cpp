#include "toolb.h"
#include<iostream>
// new BN
IppsBigNumState* nBN(int dS, const Ipp32u* A)
// new (positive) BigNum
{ // dS - data Size in Ipp32u words,
    int cS; // A - data Array, cS - BigNum contex Size
    ippsBigNumGetSize(dS, &cS);// get the BigNum context Size
    IppsBigNumState* BN = (IppsBigNumState*)( new Ipp8u [cS] );
    ippsBigNumInit(dS, BN); // BigNum context initialization
    if(A)
    ippsSet_BN(IppsBigNumPOS, dS, A, BN); // set the sign
    return BN; // and the value
}
// new negative BN
IppsBigNumState* nBNneg(int dS, const Ipp32u* A)
{
    int size;
    ippsBigNumGetSize(dS, &size);
    IppsBigNumState* BN = (IppsBigNumState*)( new Ipp8u [size] );
    ippsBigNumInit(dS, BN);
    if(A)
    ippsSet_BN(IppsBigNumNEG, dS, A, BN);
    return BN;
}
// array of Ipp32u random numbers, using rand() from stdlib
Ipp32u* randIpp32u(Ipp32u* X, int size)
{
    for(int n=0; n<size; n++)
    X[n] = (rand()<<16) + rand();
    return X;
}

// new PRNG, with default values of modulus, initial hash,
// entropy augment
IppsPRNGState* nPRNG(int sBits)
{
    int sSize =(sBits+31)>>5;
    Ipp32u* seed = new Ipp32u [sSize];
    // Ipp32u* augm = new Ipp32u [sSize];
    // Ipp32u* h0 = new Ipp32u [sSize];
    // Ipp32u* mod = new Ipp32u [sSize];
    int bnSize;
    IppsBigNumState* Tmp;
    ippsPRNGGetSize(&bnSize);
    IppsPRNGState* Ctx = (IppsPRNGState*)( new Ipp8u [bnSize] );
    ippsPRNGInit(sBits, Ctx);
    ippsPRNGSetSeed(Tmp=nBN(sSize,randIpp32u(seed,sSize)), Ctx);
    //ippsPRNGSetAugment(Tmp=nBN(sSize,randIpp32u(augm,sSize)),Ctx);
    //delete [] (Ipp8u*)Tmp;
    //ippsPRNGSetH0(Tmp=nBN(sSize,randIpp32u(h0,sSize)),Ctx);
    //delete [] (Ipp8u*)Tmp;
    //ippsPRNGSetModulus(Tmp=nBN(sSize,randIpp32u(mod,sSize)),Ctx);
    delete [] (Ipp8u*)Tmp;
    delete [] seed;
    // delete [] augm;
    // delete [] h0;
    // delete [] mod;
    return Ctx;
}
// new prime gen
//IppsPrimeState* nPrimG(int Bits,IppsPRNGState* Rand)
IppsPrimeState* nPrimG(int Bits)
{
    int size;
    ippsPrimeGetSize(Bits, &size);
    IppsPrimeState* PrimCtx = (IppsPrimeState*)( new Ipp8u [size] );
    ippsPrimeInit(Bits, PrimCtx);
    return PrimCtx;
}

/*
// new RSA
IppsRSAState* nRSA(int NbitS, int PbitS, IppRSAKeyType Ktype)
{
    int size;
    ippsRSAGetSize(NbitS,PbitS, Ktype, &size);
    IppsRSAState* Ctx = (IppsRSAState*)( new Ipp8u [size] );
    ippsRSAInit(NbitS,PbitS, Ktype, Ctx);
    return Ctx;
}
*/
// new DLP
IppsDLPState* nDLP(int PbitS, int QbitS)
{
    int size;
    ippsDLPGetSize(PbitS, QbitS, &size);
    IppsDLPState *Ctx= (IppsDLPState *)new Ipp8u[ size ];
    ippsDLPInit(PbitS, QbitS, Ctx);
    return Ctx;
}
// new ECCP
IppsECCPState* nECCP(int bitS)
{
    int size;
    ippsECCPGetSize(bitS,&size);
    IppsECCPState* ECCP=(IppsECCPState *)(new Ipp8u [size]);
    ippsECCPInit(bitS,ECCP);
    return ECCP;
}
/*
// new ECCB
IppsECCBState* nECCB(int bitS)
{
    int size;
    ippsECCBGetSize(bitS,&size);
    IppsECCBState* ECCB=(IppsECCBState *)(new Ipp8u [size]);
    ippsECCBInit(bitS,ECCB);
    return ECCB;
}
*/
// new ECCP point
IppsECCPPointState* nECCPPoint(int bitS,int Psize)
{ 
    IppsECCPPointState* Point=(IppsECCPPointState *)(new Ipp8u [Psize]);
    ippsECCPPointInit(bitS,Point);
    return Point;
}

/*
// new ECCB point
IppsECCBPointState* nECCBPoint(int bitS,int Psize)
{ 
    IppsECCBPointState* Point=(IppsECCBPointState *)(new Ipp8u [Psize]);
    ippsECCBPointInit(bitS,Point);
    return Point;
}
*/
// type BN
void tBN(const char* Msg,const IppsBigNumState* BNR)
{
    using namespace std;
    int sBNR; // size of BigNum
    ippsGetSize_BN(BNR,&sBNR); // getting size
    IppsBigNumSGN sgn; // sign of BigNum
    Ipp32u* dBNR=new Ipp32u[sBNR]; // space for BNR data
    ippsGet_BN(&sgn,&sBNR,dBNR,BNR); // getting sign and data
    int size=sBNR;
    IppsBigNumState* BN=nBN(size,dBNR);// neglecting sign
    Ipp8u* vBN = new Ipp8u [size*4]; // which is typed below
    ippsGetOctString_BN(vBN, size*4, BN);
    if(Msg)
    cout << Msg; // header
    cout.fill('0');
    cout << hex ;
    if(sgn==0) cout<< "-"; // sign
    for(int n=0; n<size*4; n++){
        cout.width(2);
        cout <<(int)vBN[n]; // value
    }
    cout << dec;
    cout.fill(' ');
    cout << endl;
    delete [] vBN; 
}
