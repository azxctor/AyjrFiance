/**
 * Copyright ArgusHealth. All rights reserved.
 */
package com.hengxin.platform.report;

import java.io.OutputStream;

/**
 * Supports null output device. This class is intended to be used within the
 * library.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
final class NullOutputStream
	extends OutputStream {

	public static final NullOutputStream INSTANCE = new NullOutputStream();

	private NullOutputStream() {
	}

	/**
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) {
	}

	/**
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) {
	}
}
