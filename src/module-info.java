module src {
	requires jpbc.api;
	requires jpbc.plaf;
	requires gson;
	requires commons.codec;
	requires junit;
	requires jpbc.pbc;
	requires java.desktop;
	requires java.logging;
    requires bcprov.jdk16;
    requires jna;
    requires jpbc.benchmark;
    requires jpbc.crypto;
    requires jpbc.mm;
	requires org.knowm.xchart;
	exports tests;	
}
