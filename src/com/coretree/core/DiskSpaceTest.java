package com.coretree.core;

import java.io.File;

public class DiskSpaceTest {

	public static void main(String[] args) {
        System.out.println(args[0]);
    	File file = new File(args[0]);
        long freespace = file.getFreeSpace(); 

        System.out.println("Disk Free Space :" + freespace);
        System.out.println("Disk Free Space (GB):" + freespace / 1024 / 1024 / 1024);
	}
}
