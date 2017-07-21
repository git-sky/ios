package cn.com.sky.ios.jdk.nio;

/**
 * <pre>
 * 
 *  Channel的子类分为两大块：
 * 
 * 1. FileChannel: 针对文件IO的Channel，可以通过FileInputStream、FileOutputStream和RandomAccessFile来获得，不支持非阻塞模式，
 * 进而也就不支持readiness selection。
 * 
 * 2. SelectableChannel: 除File以外，像对Socket IO做支持的Channel都属于SelectableChannel，支持非阻塞模式和readiness selection。
 * 
 * </pre>
 */

public class info {

}
