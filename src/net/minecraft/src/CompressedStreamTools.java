package net.minecraft.src;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressedStreamTools {
	public static NBTTagCompound func_1138_a(InputStream inputstream) throws IOException {
		DataInputStream datainputstream = new DataInputStream(new GZIPInputStream(inputstream));

		NBTTagCompound var3;
		try {
			NBTTagCompound nbttagcompound = func_1141_a(datainputstream);
			var3 = nbttagcompound;
		} finally {
			datainputstream.close();
		}

		return var3;
	}

	public static void writeGzippedCompoundToOutputStream(NBTTagCompound nbttagcompound, OutputStream outputstream) throws IOException {
		DataOutputStream dataoutputstream = new DataOutputStream(new GZIPOutputStream(outputstream));

		try {
			func_1139_a(nbttagcompound, dataoutputstream);
		} finally {
			dataoutputstream.close();
		}

	}

	public static NBTTagCompound func_1140_a(byte[] abyte0) throws IOException {
		DataInputStream datainputstream = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(abyte0)));

		NBTTagCompound var3;
		try {
			NBTTagCompound nbttagcompound = func_1141_a(datainputstream);
			var3 = nbttagcompound;
		} finally {
			datainputstream.close();
		}

		return var3;
	}

	public static byte[] func_1142_a(NBTTagCompound nbttagcompound) throws IOException {
		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		DataOutputStream dataoutputstream = new DataOutputStream(new GZIPOutputStream(bytearrayoutputstream));

		try {
			func_1139_a(nbttagcompound, dataoutputstream);
		} finally {
			dataoutputstream.close();
		}

		return bytearrayoutputstream.toByteArray();
	}

	public static NBTTagCompound func_1141_a(DataInput datainput) throws IOException {
		NBTBase nbtbase = NBTBase.readTag(datainput);
		if(nbtbase instanceof NBTTagCompound) {
			return (NBTTagCompound)nbtbase;
		} else {
			throw new IOException("Root tag must be a named compound tag");
		}
	}

	public static void func_1139_a(NBTTagCompound nbttagcompound, DataOutput dataoutput) throws IOException {
		NBTBase.writeTag(nbttagcompound, dataoutput);
	}
}
