package com.example.terminal;

import android.text.TextUtils;
import android.widget.EditText;

import java.nio.charset.StandardCharsets;

import javax.inject.Inject;


public class Utils {

    public final String STX = Character.toString((char)2);
    public final String ETX = Character.toString((char)3);
    public final String NAK = Character.toString((char)21);
    public final String CR = Character.toString((char)13);
    public final String ACK = Character.toString((char)6);

    @Inject
    public Utils(){}

    public byte[] padByte(String s) {

        byte[] b = s.getBytes(StandardCharsets.ISO_8859_1);

        byte[] b_bigger = new byte[b.length + 1];

        System.arraycopy(b, 0, b_bigger, 0, b.length);

        String str = new String(b, StandardCharsets.ISO_8859_1);

        return b_bigger;
    }

    public char calcCheckSum(String s) {
        int sum = 0;

        for (char c : s.toCharArray()) {
            int a = (c);
            sum = (sum ^ a) % 0x100;
        }

        sum |= 128;

        byte i = (byte) sum;
        int unsigned_i = (i & (0xff));
        return (char) unsigned_i;
    }

    public static Boolean validateEditText(EditText editText, String error, int min_length){
        String str = editText.getText().toString();
        if(min_length == -1) {
            if (!TextUtils.isEmpty(str)) return true;
            else editText.setError(error);
        }
        else{
            if (!TextUtils.isEmpty(str) && str.length() >= min_length) return true;
            else editText.setError(error);
        }
        return false;
    }

}
