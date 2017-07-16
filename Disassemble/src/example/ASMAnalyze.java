/*
 詰まったところ
1.
13a : 32 8f ff 01　6b 	cvtwl $0x01ff,(r11)
↓
13A : 32 8F FF 01 6B F6 AC 04  : CVTWL 0xF66B01FF, 0x04(AP)
イミディエートモードの処理



4.
150:　　31 47 ff    	brw 0x9a
↓
150 :  31 47  : BRW 0x199

5.
217:	8a 8f 7f ab 0c 	bicb2 $0x7f,0xc(r11)

6.
 2cb:	f1 01 01 ef 	acbl $0x1,$0x1,0x75c,0x281
 2cf:	89 04 00 00 
 2d3:	ac ff 
↓
 2CB :  F1 01 01 EF 89 04 00 00 AC  : ACBL $0x01, $0x01, 0x75C, 0x280
 2D4 :  FF 
 
 7.
  3ce:	c3 5b ab fc 	subl3 r11,0xfffffffc(r11),r0
  3d2:	50 
 ↓
  3CE :  C3 5B AB FC 50  : SUBL3 R11, 0xFC(R11), R0
  
  8.
   C2 8F 80 : 0xffff808f(r2)
   ↓
   ADDL2 $0x0F, 0x808F(R2)
   
   9.
   AD FC : 0xfffffffc(fp)
   ↓
　　AD FC : 0xFC(FP)

   10.
   ad fc : 0xfffffffc(fp)
   ↓
   AD FC  : 0xFC(FP)
   
   11.
   30 31 32    bsbw 0x3344
   ↓
   30 31  : BSBW 0x143
   
   12.
   211: f1 00 56 52 acbl $0x0,r6,r2,0x204
   215: ed ff 
   ↓
   211 :  F1 00 56 52 ED  : ACBL $0x00, R6, R2, 0x203
   
 */


package example;

public class ASMAnalyze {
	
	private byte[] buf;
	private int pos;
	private int textSize;
	
	public ASMAnalyze(byte[] buf) {
		this.buf = buf;
		pos = 0;
	}
	
	/***************************************
	 *命令判別
	 *******************************************/
	
	public void analyze() {
		byte ope;
		while(true) {
			if(pos == 0) {
				header();
			}
			ope = buf[pos++];
			System.out.print(String.format("%4X :  %02X ", pos - 33, ope));
			switch(ope) {
			case 0:
				System.out.print(": HALT");
				break;
				
			case (byte)0x01:
				nop();
				break;
				
			case (byte)0x02:
				//rei();
				break;
			
			case (byte)0x03:
				//bpt();
				break;
				
			case (byte)0x04:
				ret();
				break;
				
			case (byte)0x05:
				rsb();
				break;
				
			case (byte)0x06:
				//ldpctx();
				break;
			
			case (byte)0x07:
				//svpctx();
				break;
				
			case (byte)0x08:
				cvtps();
				break;
				
			case (byte)0x09:
				//cvtsp();
				break;
			
			case (byte)0x0A:
				//index();
				break;
			
			case (byte)0x0B:
				//crc();
				break;
				
			case (byte)0x0C:
				prober();
				break;
				
			case (byte)0x0D:
				//probew();
				break;
				
			case (byte)0x0E:
				insque();
				break;
				
			case (byte)0x0F:
				remque();
				break;
				
			case (byte)0x10:
				//bsbb();
				break;
				
			case (byte)0x11:
				brb();
				break;
				
			case (byte)0x12:
				bneq();
				break;
				
			case (byte)0x13:
				beql();
				break;
				
			case (byte)0x14:
				bgtr();
				break;
				
			case (byte)0x15:
				bleq();
				break;
				
			case (byte)0x16:
				//jsb();
				break;
				
			case (byte)0x17:
				jmp();
				break;
				
			case (byte)0x18:
				bgeq();
				break;
				
			case (byte)0x19:
				blss();
				break;
				
			case (byte)0x1C:
				bvc();
				break;
				
			case (byte)0x1E:
				bcc();
				break;
				
			case (byte)0x28:
				movc3();
				break;
				
			case (byte)0x2B:
				spanc();
				break;
				
			case (byte)0x2E:
				movtc();
				break;
				
			case (byte)0x30:
				bsbw();
				break;
				
			case (byte)0x31:
				brw();
				break;
				
			case (byte)0x32:
				cvtwl();
				break;
				
			case (byte)0x33:
				cvtwb();
				break;
				
			case (byte)0x36:
				cvtpl();
				break;
				
			case (byte)0x38:
				editpc();
				break;
				
			case (byte)0x3A:
				locc();
				break;
				
			case (byte)0x3B:
				skpc();
				break;
				
			case (byte)0x4D:
				cvtwf();
				break;
				
			case (byte)0x50:
				movf();
				break;
					
			case (byte)0x5E:
				remqhi();
				break;
				
			case (byte)0x61:
				addd3();
				break;
				
			case (byte)0x63:
				subd3();
				break;
				
			case (byte)0x65:
				muld3();
				break;
				
			case (byte)0x67:
				divd3();
				break;
			
			case (byte)0x68:
				cvtdb();
				break;
				
			case (byte)0x6C:
				cvtbd();
				break;
				
			case (byte)0x76:
				cvtdf();
				break;
				
			case (byte)0x78:
				ashl();
				break;
				
			case (byte)0x88:
				bisb2();
				break;
				
			case (byte)0x8A:
				bicb2();
				break;
				
			case (byte)0x8B:
				bicb3();
				break;
				
			case (byte)0x8F:
				caseb();
				break;
			
			case (byte)0x90:
				movb();
				break;
				
			case (byte)0x91:
				cmpb();
				break;
			
			case (byte)0x93:
				bitb();
				break;
				
			case (byte)0x94:
				clrb();
				break;
				
			case (byte)0x98:
				cvtbl();
				break;
				
			case (byte)0x9A:
				movzbl();
				break;
				
			case (byte)0x9E:
				movab();
				break;
				
			case (byte)0xA9:
				bisw3();
				break;
				
			case (byte)0xAB:
				bicw3();
				break;
				
			case (byte)0xAD:
				xorw3();
				break;
				
			case (byte)0xBA:
				popr();
				break;
				
			case (byte)0xBC:
				chmk();
				break;
				
			case (byte)0xC0:
				addl2();
				break;
				
			case (byte)0xC1:
				addl3();
				break;
				
			case (byte)0xC2:
				subl2();
				break;
				
			case (byte)0xC3:
				subl3();
				break;
				
			case (byte)0xC6:
				divl2();
				break;
				
			case (byte)0xC7:
				divl3();
				break;
				
			case (byte)0xC8:
				bisl2();
				break;
				
			case (byte)0xC9:
				bisl3();
				break;
				
			case (byte)0xCA:
				bicl2();
				break;
				
			case (byte)0xCB:
				bicl3();
				break;
				
			case (byte)0xCE:
				mnegl();
				break;
				
			case (byte)0xD0:
				movl();
				break;
				
			case (byte)0xD1:
				cmpl();
				break;
				
			case (byte)0xD4:
				clrf();
				break;
			
			case (byte)0xD5:
				tstl();
				break;
				
			case (byte)0xD6:
				incl();
				break;
				
			case (byte)0xD7:
				decl();
				break;
				
			case (byte)0xDD:
				pushl();
				break;
				
			case (byte)0xDE:
				moval();
			 	break;
				
			case (byte)0xDF:
				pushal();
				break;
				
			case (byte)0xE0:
				bbs();
				break;
				
			case (byte)0xE8:
				blbs();
				break;
				
			case (byte)0xE9:
				blbc();
				break;
				
			case (byte)0xE1:
				bbc();
				break;
				
			case (byte)0xEF:
				extzv();
				break;
				
			case (byte)0xF1:
				acbl();
				break;
				
			case (byte)0xF6:
				cvtlb();
				break;
				
			case (byte)0xF9:
				cvtlp();
				break;
				
			case (byte)0xFB:
				calls();
				break;
				
			}
			
			if(pos == textSize + 33) {
				return;
			}
		
			System.out.println();
			
		}
		
	}
	
	/***************************************
	 *命令 
	 *******************************************/
	
	private void movl() { // opcode src.rx dst.wx : dst ← src
		String arg1 = readOperand(); //src
		String arg2 = readOperand(); //dst
		System.out.print(" : MOVL " + arg1 + ", " + arg2);
	}
	
	private void chmk() { //opcode code.rw
		String arg = readOperand();
		System.out.print(" : CHMK " + arg);
	}
	
	private void subl2() { //opcode sub.rx dif.mx : dif ← dif - sub
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : SUBL2  " + arg1 + ", " + arg2);
	}
	
	private void movab() {
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : MOVAB " + arg1 + ", " + arg2);
	}
	
	private void tstl() { //opcode src.rx : src - 0
		String arg = readOperand();
		System.out.print(" : TSTL " + arg);
	}
	
	private void bneq() { //opcode displ.bb : if confition then PC ← SEXT(displ)
		String arg = relativeOperand();
		System.out.print(" : BNEQ " + arg);
	}
	
	private void cmpl() { //opcode src1.rx src2.rx : src1 - src2
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : CMPL " + arg1 + ", " + arg2);
	}
	
	private void blss() { //opcode displ.bb : if condition then PC ← PC + SEXT(displ)
		String arg = relativeOperand();
		System.out.print(" : BLSS " + arg);
	}
	
	private void calls() { //opcode numarg.rl, dst.ab
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : CALLS " + arg1 + ", " + arg2);
	}
	
	private void pushl() { //opcode src.rl
		String arg = readOperand();
		System.out.print(" : PUSHL " + arg);
	}
	
	private void cvtdb() { //opcode src.rx, dst.wy
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : CVTDB " + arg1 + ", " + arg2);
	}
	
	private void cvtbd() { //opcode src.rx, dst.wy
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : CVTBD " + arg1 + ", " + arg2);
	}
	
	private void ret() {
		System.out.print(" : RET");
	}
	
	private void rsb() {
		System.out.print(" : RSB");
	}
	
	private void nop() {
		System.out.print(" : NOP");
	}
	
	private void bcc() { //opcode displ.bb
		String arg = relativeOperand();
		System.out.print(" : BCC " + arg);
	}
	
	private void jmp() { //opcode dst.ab : PC ← dst
		String arg = relativeOperand();
		System.out.print(" : JMP " + arg);
	}
	
	private void mnegl() { //opcode src.rx,dst.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : MNEGL " + arg1 + ", " + arg2);
	}
	
	private void cvtps() { //opcode srclen.rw, srcaddr.ab, dstlen.rw, dstaddr.ab
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		String arg4 = readOperand();
		System.out.print(" : CVTPS " + arg1 + ", " + arg2 + ", " + arg3 + ", " + arg4);
	}
	
	private void incl() { //opcode sum.mx
		String arg = readOperand();
		System.out.print(" : INCL " + arg);
	}
	
	private void cvtlb() { //opcode src.rx, dst.wy
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : CVTLB " + arg1 + ", " + arg2);
	}
	
	private void cvtbl() { //opcode src.rx, dst.wy
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : CVTBL " + arg1 + ", " + arg2);
	}
	
	private void brb() { //opcode displ.bx
		String arg = relativeOperand();
		System.out.print(" : BRB " + arg);
	}
	
	private void pushal() { //opcode src.ax
		String arg = readOperand();
		System.out.print(" : PUSHAL " + arg);
	}
	
	private void remque() { //opcode entry.ab, addr.wl
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : REMQUE " + arg1 + ", " + arg2);
	}
	
	private void bicw3() { //opcode mask.rx, src.rx, dst.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : BICW3 " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void beql() { //opcode displ.bb
		String arg = relativeOperand();
		System.out.print(" : BEQL " + arg);
	}
	
	private void bisb2() { //opcode mask.rx, dst.mx
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : BISB2 " + arg1 + ", " + arg2);
	}
	
	private void bbc() { //opcode pos.rl, base.vb, displ.bb
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = relativeOperand();
		System.out.print(" : BBC " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void clrf() { //opcode dst.wx
		String arg = readOperand();
		System.out.print(" : CLRF " + arg);
	}
	
	private void moval() { //opcode src.ax,dst.wl
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : MOVAL " + arg1 + ", " + arg2);
	}
	
	private void subl3() { //opcode sub.rx, min.rx, dif.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : SUBL3 " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void bleq() { //opcode displ.bb
		String arg = relativeOperand();
		System.out.print(" : BLEQ " + arg);
	}
	
	private void cvtwl() { //opcode src.rx, dst.wy
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : CVTWL " + arg1 + ", " + arg2);
	}
	
	private void brw() { //opcode displ.bx
		String arg = relativeWordRead();
		System.out.print(" : BRW " + arg);
	}
	
	private void insque() { //opcode entry.ab, pred.ab
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : INSQUE " + arg1 + ", " + arg2);
	}
	
	private void prober() { //opcode mode.rb, len.rw, base.ab
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : PROBER " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void addl2() { //opcode add.rx sum.mx
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : ADDL2 " + arg1 + ", " + arg2);
	}
	
	private void bitb() { //opcode mask.rx, src.rx
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : BITB " + arg1 + ", " + arg2);
	}
	
	private void bbs() { //opcode pos.rl, base.vb, displ.bb
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = relativeOperand();
		System.out.print(" : BBS " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void bgeq() { //opcode displ.bb
		String arg = relativeOperand();
		System.out.print(" : BGEQ " + arg);
	}
	
	private void bicb2() { //opcode mask.rx, dst.mx
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : BICB2 " + arg1 + ", " + arg2);
	}
	
	private void bisl3() { //opcode mask.rx, src.rx, dst.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : BISL3 " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void addl3() { //opcode add1.rx, add2.rx, sum.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : ADDL3 " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void divl3() { //opcode divr.rx, divd.rx, quo.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : DIVL3 " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void blbc() { //opcode src.rl, displ.bb
		String arg1 = readOperand();
		String arg2 = relativeOperand();
		System.out.print(" : BLBC " + arg1 + ", " + arg2);
	}
	
	private void ashl() { //opcode cnt.rb, src.rx, dst.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : ASHL " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void bicl3() { //opcode mask.rx, src.rx, dst.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : BICL3 " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void bgtr() { //opcode displ.bb
		String arg = relativeOperand();
		System.out.print(" : BGTR " + arg);
	}
	
	private void acbl() { //opcode limit.rx, add.rx, index.mx, displ.bw
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		String arg4 = relativeWordRead();
		System.out.print(" : ACBL " + arg1 + ", " + arg2 + ", " + arg3 + ", " + arg4);
	}
	
	private void divl2() { //opcode divr.rx, quo.mx
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : DIVL2 " + arg1 + ", " + arg2);
	}
	
	private void bisl2() { //opcode mask.rx, dst.mx
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : BISL2 " + arg1 + ", " + arg2);
	}
	
	private void decl() { //opcode dif.mx
		String arg = readOperand();
		System.out.print(" : DECL " + arg);
	}
	
	private void remqhi() { //opcode header.aq, addr.wl
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : REMQHI " + arg1 + ", " + arg2);
	}
	
	private void bicl2() { //opcode mask.rx, dst.mx
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : BICL2 " + arg1 + ", " + arg2);
	}
	
	private void movb() { //opcode src.rx, dst.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : MOVB " + arg1 + ", " + arg2);
	}
	
	private void cmpb() { //opcode src1.rx src2.rx
		String arg1 = readOperand();
		String arg2 = twoByteRead();
		System.out.print(" : CMPB " + arg1 + ", " + arg2);
	}
	
	private void blbs() { //opcode src.rl, displ.bb
		String arg1 = readOperand();
		String arg2 = relativeOperand();
		System.out.print(" : BLBS " + arg1 + ", " + arg2);
	}
	
	private void subd3() { //opcode sub.rx, min.rx, dif.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : SUBD3 " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void divd3() { //opcode divr.rx, divd.rx, quo.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : DIVD3 " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void spanc() { //opcode len.rw, addr.ab, tbladdr.ab, mask.rb
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		String arg4 = readOperand();
		System.out.print(" : SPANC " + arg1 + ", " + arg2 + ", " + arg3 + ", " + arg4);
	}
	
	private void bsbw() { //opcode displ.bx　  displ:相対パス:2byte
		String arg = wordRead();
		System.out.print(" : BSBW " + arg);
	}
	
	private void cvtwb() { //opecode src.rx, dst.wy
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : CVTWB " + arg1 + ", " + arg2);
	}
	
	private void cvtpl() { //opcode srclen.rw, srcaddr.ab, dst.wl
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : CVTPL " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void addd3() { //opcode add1.rx, add2.rx, sum.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : ADDD3 " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void muld3() { //opcode mulr.rx, muld.rx, prod.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : MULD3 " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void bicb3() { //opcode mask.rx, src.rx, dst.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : BICB3 " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void movf() { //opcode src.rx, dst.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : MOVF " + arg1 + ", " + arg2);
	}
	
	private void cvtwf() { //opcode src.rx, dst.wy
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : CVTWF " + arg1 + ", " + arg2);
	}
	
	private void popr (){ //opcode mask.rw
		String arg = readOperand();
		System.out.print(" : POPR " + arg);
	}
	
	private void movtc() { //opcode srclen.rw, srcaddr.ab, fill.rb, tbladdr.ab, dstlen.rw, dstaddr.ab
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		String arg4 = readOperand();
		String arg5 = readOperand();
		String arg6 = readOperand();
		System.out.print(" : MOVTC " + arg1 + ", " + arg2 + ", " + arg3 + ", " + arg4 + ", " + arg5 + ", " + arg6);
	}
	
	private void clrb() { //opcode dst.wx
		String arg = twoByteRead();
		System.out.print(" : CLRB " + arg);
	}
	
	private void xorw3() { //opcode mask.rx, src.rx, dst.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : XORW3 " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void cvtdf() { //opcode src.rx, dst.wy
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : CVTDF " + arg1 + ", " + arg2);
	}
	
	private void caseb() { //opcode selector.rx, base.rx, limit.rx
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : CASEB " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void movzbl() { //opcode src.rx, dst.wy
		String arg1 = readOperand();
		String arg2 = readOperand();
		System.out.print(" : MOVZBL " + arg1 + ", " + arg2);
	}
	
	private void movc3() { //opcode len.rw, srcaddr.ab, dstaddr.ab
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : MOVC3 " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void locc() { //opcode char.rb, len.rw, addr.ab
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : LOCC " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void extzv() { //opcode pos.rl, size.rb, base.vb, dst.wl
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		String arg4 = readOperand();
		System.out.print(" : EXTZV " + arg1 + ", " + arg2 + ", " + arg3 + ", " + arg4);
	}
	
	private void skpc() { //opcode char.rb, len.rw, addr.ab
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : SKPC " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void bisw3() { //opcode mask.rx, src.rx, dst.wx
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : BISW3 " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void cvtlp() { //opcode src.rl, dstlen.rw, dstaddr.ab
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		System.out.print(" : CVTLP " + arg1 + ", " + arg2 + ", " + arg3);
	}
	
	private void editpc() { //opcode srclen.rw, srcaddr.ab, pattern.ab, dstaddr.ab
		String arg1 = readOperand();
		String arg2 = readOperand();
		String arg3 = readOperand();
		String arg4 = readOperand();
		System.out.print(" : EDITPC " + arg1 + ", " + arg2 + ", " + arg3 + ", " + arg4);
	}
	
	private void bvc() { //opcode displ.bb
		String arg = relativeOperand();
		System.out.print(" : BVC " + arg);
	}
	
	
	
	/***************************************
	 *オペランドの読み取り
	 *******************************************/
	
	private String readOperand() {
		byte b = buf[pos++]; //b = b1b2
		byte b1 = (byte)((b >> 4) & (byte)0xf);
		byte b2 = (byte)(b & (byte)0xf);
		return operand(b1, b2);
	}
	
	private String relativeOperand() {
		byte b = buf[pos++]; //b = b1b2
		byte b1 = (byte)((b >> 4) & (byte)0xf);
		byte b2 = (byte)(b & (byte)0xf);
		
		System.out.print(String.format("%02X ", b));
		
		if(iProgramCounterAdressingMode(b1, b2)) { //プログラム・カウンタ・アドレッシング・モード
			int pcamNum = programCounterAdressingMode();
			int nextPos = pos + 1 - 33;
			int num = pcamNum + nextPos;
			return String.format("0x%X", num);
		} else {	
			int nextPos = pos + 1 - 33;
			int num = b + nextPos;
			return String.format("0x%X", num);
		}
	}
	
	private String operand(byte b1, byte b2) {
		System.out.print(String.format("%X%X ", b1, b2));
		switch(b1) {
		case (byte)0x4: //インデックスモード
			StringBuilder sb4 = new StringBuilder();
			sb4.append(readOperand());
			sb4.append("[");
			sb4.append(register(b2));
			sb4.append("]");
			return sb4.toString();
			
		
		case (byte)0x5: //レジスタ
			return register(b2);
			
		case (byte)0x6: //レジスタ・ディファード・モード
			StringBuilder sb6 = new StringBuilder();
			sb6.append("(");
			sb6.append(register(b2));
			sb6.append(")");
			return sb6.toString();
			
		case (byte)0x7: //オートデクリメント
			switch(b2) {
			case (byte)0xf: //プログラムカウンタアドレッシングモード
				return "0x" + String.valueOf(programCounterAdressingMode());
				
			default:
				StringBuilder sb8 = new StringBuilder();
				sb8.append("-(");
				sb8.append(register(b2));
				sb8.append(")");
				return sb8.toString();
			}
			
		case (byte)0x8: //オートインクリメントモード
			switch(b2) {
			case (byte)0xf: //イミディエート・モード
				return "0x" + String.format("%X", programCounterAdressingMode());
				
			default:
				StringBuilder sb8 = new StringBuilder();
				sb8.append("(");
				sb8.append(register(b2));
				sb8.append(")+");
				return sb8.toString();
			}
		
		case (byte)0x9: //オートインクリメントディファード
			if(b2 == (byte)0xf) { //アブソリュート
				
			} else {
				StringBuilder sb9 = new StringBuilder();
				sb9.append("@(");
				sb9.append(register(b2));
				sb9.append(")+");
				return sb9.toString();
			}
			
			
		case (byte)0xa: //バイト・ディスプレイメント
			StringBuilder sba = new StringBuilder();
			sba.append(twoByteRead());
			sba.append("(");
			sba.append(register(b2));
			sba.append(")");
			return sba.toString().replaceAll("\\$", "");
			
		case (byte)0xb: //バイト・ディスプレイメント・ディファード
			StringBuilder sbb = new StringBuilder();
			sbb.append("*");
			sbb.append(readOperand());
			sbb.append("(");
			sbb.append(register(b2));
			sbb.append(")");
			return sbb.toString().replaceAll("\\$", "");
			
		case (byte)0xC: //ワード・ディスプレイメント・モード(2byte)
			if(b2 == (byte)0xf) { //ワード・リラティブ
				return wordRead();
			} else {
				StringBuilder sbc = new StringBuilder();
				sbc.append(fourByteRead());
				sbc.append("(");
				sbc.append(register(b2));
				sbc.append(")");
				return sbc.toString();
			}
				
		case (byte)0xd://ワード・ディスプレイメント・ディファード(2byte)
			StringBuilder sbd = new StringBuilder();
			sbd.append("*");
			sbd.append(fourByteRead());
			sbd.append("(");
			sbd.append(register(b2));
			sbd.append(")");
			return sbd.toString();
			
		case (byte)0xe: //ロングワード・ディスプレイスメント・モード(4byte)
			switch(b2) {
			case (byte)0xf: //プログラムカウンタアドレッシングモード
				int operand = programCounterAdressingMode();
				
				int nextPos = pos + 1 - 33;
				
				int num = operand + nextPos;
				return String.format("0x%X", num);
			}
		
		
		case (byte)0xf: //ロングワード・ディスプレイメント・ディファード
			switch(b2) {
			case (byte)0xf: //ロングワード・リラティブ・ディファード
				int operand = programCounterAdressingMode();
			
				int nextPos = pos + 1 - 33;
				
				int num = operand + nextPos;
				return String.format("0x%X", num);
			default:
				StringBuilder sbf = new StringBuilder();
				sbf.append("*");
				sbf.append(eightByteRead());
				sbf.append("(");
				sbf.append(register(b2));
				sbf.append(")");
				return sbf.toString();
			}
			
		default:
			return String.format("$0x%X%X", b1, b2);	
		}
	}
	
	private String twoByteRead() {
		StringBuilder sb = new StringBuilder();
		byte buf1 = buf[pos++];
		byte b1 = (byte)((buf1 >> 4) & (byte)0xf);
		byte b2 = (byte)(buf1 & (byte)0xf);
		switch(b1) {
		case (byte)0x6:
			StringBuilder sb6 = new StringBuilder();
			sb6.append("(");
			sb6.append(register(b2));
			sb6.append(")");
			return sb6.toString();
		
		case (byte)0x8:
			if(b2 == (byte)0xF) {
				byte b = buf[pos++];
				System.out.print(String.format("%02X ", b));
				return String.format("$0x%2X", b);
			} else {
				System.out.print(String.format("%02X ", buf1));
				return "$0x" + String.format("%02X", buf1);
			}
		default:
			System.out.print(String.format("%02X ", buf1));
			return "$0x" + String.format("%02X", buf1);
		}
		
	}
	
	/*
	 case (byte)0x8: //オートインクリメントモード
			switch(b2) {
			case (byte)0xf: //イミディエート・モード
				return "0x" + String.format("%X", programCounterAdressingMode());
				
			default:
				StringBuilder sb8 = new StringBuilder();
				sb8.append("(");
				sb8.append(register(b2));
				sb8.append(")+");
				return sb8.toString();
			}
	 */
	
	private String fourByteRead() {
		StringBuilder sb = new StringBuilder();
		byte buf1 = buf[pos++];
		byte buf2 = buf[pos++];
		System.out.print(String.format("%02X %02X ", buf1, buf2));
		return "0x" + String.format("%02X%02X", buf2, buf1);
	}
	
	private String eightByteRead() {
		StringBuilder sb = new StringBuilder();
		byte buf1 = buf[pos++];
		byte buf2 = buf[pos++];
		byte buf3 = buf[pos++];
		byte buf4 = buf[pos++];
		System.out.print(String.format("%02X %02X %02X %02X ", buf1, buf2, buf3, buf4));
		return "0x" + String.format("%02X%02X%02X%02X", buf4, buf3, buf2, buf1);
	}
	
	private int programCounterAdressingMode() {
		byte buf1 = buf[pos++];
		byte buf2 = buf[pos++];
		byte buf3 = buf[pos++];
		byte buf4 = buf[pos++];
		
		System.out.print(String.format("%02X %02X %02X %02X ", buf1, buf2, buf3, buf4));
		
		int b1 = buf1 & 0xff;
		int b2 = (buf2 & 0xff) << 8;
		int b3 = (buf3 & 0xff) << 16;
		int b4 = (buf4 & 0xff) << 24;
		
		int buf = b1 + b2 + b3 + b4;
		
		return buf;
	}
	
	private String register(byte b) {
		switch(b) {
		case (byte)0xC: //AP
			return "AP";
		case (byte)0xD: //FP
			return "FP";
		case (byte)0xE: //SP
			return "SP";
		case (byte)0xF: //PC
			return "PC";
		default:
			return "R" + String.valueOf(b);
		}
	}
	
	private boolean iProgramCounterAdressingMode(byte b1, byte b2) {
		if(b2 != (byte)0xf) {
			return false;
		}
		if(8 <= b1 && b1 <= 15) {
			return true;
		}
		return false;
	}
	
	private String wordRead() {
		byte buf1 = buf[pos++];
		byte buf2 = buf[pos++];
		System.out.print(String.format("%02X %02X ", buf1, buf2));
		int b = (buf1 & 0xFF) + ((buf2 & 0xFF) << 8);
		int nextPos = pos + 1 - 33;
		int num = b + nextPos;
		//System.out.println("\nb : "+ b + " buf1 : " + buf1);
		return String.format("0x%X", num);
	}
	
	private String relativeWordRead() {
		byte buf1 = buf[pos++];
		byte buf2 = buf[pos++];
		System.out.print(String.format("%02X %02X ", buf1, buf2));
		//int b = (byte)((buf1 & 0xFFFF) | ((buf2 & 0xFF) << 8));
		int b = buf2 << 8;
		b |= (buf1 & 0xFF);
		int nextPos = pos + 1 - 33;
		int num = b + nextPos;
		//System.out.println("\nb : "+ b + " buf1 : " + buf1 + " buf2 : " + buf2);
		return String.format("0x%X", num);
	}
	
	private void header() {
		byte[] b = new byte[64];
		String[] explain = {"マジックナンバー", "テキストセグメントのサイズ", "初期化データのサイズ", "未初期化データのサイズ", 
				"シンボルテーブルのサイズ", "プログラムの実行開始アドレス", "テキスト再配置情報のサイズ", "データ再配置情報のサイズ"};
		
		do {
			b[pos] = buf[pos++];
		} while(pos < 32);
		
		System.out.println("*** header ***");
		int num = 0;
		
		/*
		 * テキストサイズ取得
		 */
		
		byte buf1 = b[4];
		byte buf2 = b[5];
		byte buf3 = b[6];
		byte buf4 = b[7];
		textSize = (buf1 & 0xFF) + ((buf2 & 0xFF) << 8) + ((buf3 & 0xFF) << 16) + ((buf4 & 0xFF) << 24);
		System.out.println("textSize : " + String.format("%X", textSize));
		
		for(int i = 0; i < 32; i ++) {
			System.out.print(String.format("%02X ", b[i]));
			if(i % 4 == 3) {
				System.out.println(" : " + explain[num++]);
			}
		}
		System.out.println("*** header ***\n\n");
	}
/*
        u_int32_t       a_midmag;    マジックナンバー 
        u_int32_t       a_text;      テキストセグメントのサイズ 
        u_int32_t       a_data;      初期化データのサイズ 
        u_int32_t       a_bss;       未初期化データのサイズ 
        u_int32_t       a_syms;      シンボルテーブルのサイズ 
        u_int32_t       a_entry;     プログラムの実行開始アドレス 
        u_int32_t       a_trsize;    テキスト再配置情報のサイズ 
        u_int32_t       a_drsize;    データ再配置情報のサイズ 
 */
}
