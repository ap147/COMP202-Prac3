import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.*;
import java.io.*;
import java.util.regex.Pattern;

//http://www.tutorialspoint.com/java/java_using_comparator.htm
//http://stackoverflow.com/questions/31402113/why-i-cannot-split-string-with-in-java
//http://stackoverflow.com/questions/4209760/validate-an-ip-address-with-mask
//http://stackoverflow.com/questions/14669820/how-to-convert-a-string-array-to-a-byte-array-java
/*
 * prefixComparator
 *
 * this class is used when sorting an array in Java.
 */
class prefixComparator implements Comparator<prefix>
{
    public int compare(prefix a, prefix b)
    {
	/*
	 * XXX:
	 * return a value such that the array is ordered from
	 * most specific to least specific.  take a look at the
	 * documentation at
	 *
	 * http://docs.oracle.com/javase/7/docs/api/java/util/Comparator.html
	 *
	 * on how the return value from this method will impact sort order.
	 * make sure the longest prefixes are sorted so that they come
	 * first!
	 *  This method returns zero if the objects are equal.
	 *  It returns a positive value if obj1 is greater than obj2. Otherwise, a negative value is returned.
	 */
	    int result = 0;

        if(a.len > b.len)
        {
            //System.out.println("a has a greater prefix than b : a:" + a.len + " b : " + b.len);
            return 1;
        }
        else if(a.len < b.len)
        {
           // System.out.println("a has a smaller prefix than b : a:" + a.len + " b : " + b.len);
            return -1;
        }

        return result;
    }
};

/*
 * prefix
 *
 * this class holds details of a prefix: the network address, the
 * prefix length, and details of the autonomous systems that announce
 * it.
 *
 */
class prefix
{

    public int[]       net = {0,0,0,0};
    protected String easyNet;
   // public int[] net = new int[4];

    public int         len;
    public String      asn;

    public prefix(String net, int len, String asn)
    {
	/*------------------------------------------------------------------------
	 * XXX:
	 * initialise the object given the inputs.  break
	 * the network ID into four integers.
	 */
        addToNet(net);
        this.asn = asn;
        this.len = len;
    }
    private void addToNet(String x )
    {
        String arrayString[] = x.split(Pattern.quote("."));
        this.net[0] = Integer.parseInt(arrayString[0]);
        this.net[1] = Integer.parseInt(arrayString[1]);
        this.net[2] = Integer.parseInt(arrayString[2]);
        this.net[3] = Integer.parseInt(arrayString[3]);
        easyNet = x;
    }
    public String toString()
    {
	/* pretty print out of the prefix! my lecturer is kind! */
       return net[0] + "." + net[1] + "." + net[2] + "." + net[3] + "/" + len;
    }

    public boolean Eazymatch(String addr) throws UnknownHostException {



        //IP
        String       s = addr;
        Inet4Address a = (Inet4Address) InetAddress.getByName(s);
        byte [] ipByte = a.getAddress();
        int IP = ((ipByte[0] & 0xFF) << 24) |
                ((ipByte[1] & 0xFF) << 16) |
                ((ipByte[2] & 0xFF) << 8)  |
                ((ipByte[3] & 0xFF) << 0);

        //SUBNET
        Inet4Address b = (Inet4Address) InetAddress.getByName(easyNet);
        byte [] subByte = b.getAddress();
        int Subnet = ((subByte[0] & 0xFF) << 24) |
                ((subByte[1] & 0xFF) << 16) |
                ((subByte[2] & 0xFF) << 8)  |
                ((subByte[3] & 0xFF) << 0);


        int subnetBits = len;

        int mask = -1 << (32 - subnetBits);

        if((Subnet & mask) == (IP& mask))
        {
            System.out.println(addr);
            System.out.println(this.toString());
            System.out.println(this.asn);
            return true;
        }
        return false;

    }

    /*
     * match
     *
     * given an address, determine if it is found in this
     * prefix structure or not.
     */
    public boolean match(String addr)
    {
        int[] mask = {0x80, 0xC0, 0xE0, 0xF0, 0xF8, 0xFC, 0xFE, 0xFF};
        boolean Result = false;
        int match = 0;
        /*
	     * XXX:------------------------------------------------------------------------
	     * break up the address passed in as a string
	     */


        //IP
        String arrayString[] = addr.split(Pattern.quote("."));
        int result;
        int[] array = {0,0,0,0};

        array[0] = Integer.parseInt(arrayString[0]);
        array[1] = Integer.parseInt(arrayString[1]);
        array[2] = Integer.parseInt(arrayString[2]);
        array[3] = Integer.parseInt(arrayString[3]);

        int lenCounter = len;
        //COMPARE 8 Bits
        for(int i=0; i<4; i++)
        {


             // if((array[i] & mask[6]) == (net[i] & mask[6]))
               //   match++;




	    /*
	     * XXX:
	     * compare up to four different values in the dotted quad,
	     * (i.e. enough to cover this.len) to determine if this
	     * address is a match or not
	     */
        }


        if(match == 3)
        {
            return true;
        }
        return Result;
    }
    public int getLen()
    {
        return len;
    }
    protected int getMask(int position)
    {
        int[] mask = {0x80, 0xC0, 0xE0, 0xF0, 0xF8, 0xFC, 0xFE, 0xFF};
        int maskresult =0;



        return maskresult;
    }
};

class ip2as
{
    protected static ArrayList<String> ASNlist = new ArrayList<>();
    public static void main(String args[]) throws UnknownHostException {
        if(args.length < 3)
        {
	    /* always check the input to the program! */
            System.err.println("usage: ip2as <prefixes> <asnames> [ip0 ip1 .. ipN]");
            //java ip2as 20160701.ip2as.txt asnames.txt 128.30.2.155 10.110.8.71 1.0.192.1 205.204.15.1
            return;
        }

	/* read the prefix list into a list */
        ArrayList<prefix> list = new ArrayList<prefix>();
        try
        {
            BufferedReader file = new BufferedReader(new FileReader(args[0]));
            String line;

            while((line = file.readLine()) != null)
            {
		/* -------------------------------------------------XXX: add code to parse the ip2as line */
                String net, ases;
                int len;

                String array[] = line.split(" ");
                String array2[] = array[0].split("/");

                //85.29.150.0/23 21299
                //Getting "1.1.1.1/2" "1.1.1.1"
                net = array2[0];
                //Getting "1.1.1.1/2" "/"
                len = Integer.parseInt(array2[1]);
                ases = array[1];


		        /* create a new prefix object and stuff it in the list */
                prefix pf = new prefix(net, len, ases);
                list.add(pf);
            }
            file.close();
        }
        catch(FileNotFoundException e)
        {
            System.err.println("could not open prefix file " + args[0]);
            return;
        }
        catch(IOException e)
        {
            System.err.println("error reading prefix file " + args[0] + ": " +e);
        }

	/*
	 * take the list of prefixes and transform it into a sorted array
	 * i'd like to thank my lecturer for giving me this code.
	 */
        prefix []x = new prefix[list.size()];
        list.toArray(x);
        Arrays.sort(x, new prefixComparator());
	/*
	 * read in the asnames file so that we can report the
	 * network's name with its ASN
	 */
        FetchASNames(args[1]);


	/*
	 * for all IP addresses supplied on the command line, print
	 * out the corresponding ASes that announce a corresponding
	 * prefix, as well as their names.  if there is no
	 * corresponding prefix, print the IP address and then say no
	 * corresponding prefix.
	 */
	System.out.println();
        for(int i=2; i<args.length; i++)
        {
            int matched = 0;
	    /*
	     * x contains the sorted array of prefixes, organised longest
	     * to shortest prefix match
	     */
	        prefix p = x[i];
            for(int j=0; j<x.length; j++)
            {
                 p = x[j];

		/*-------------------------------------------------------------------------
		 * XXX:
		 * check if this prefix matches the IP address passed in----------------------------------------------
		 */
		        boolean Result = p.Eazymatch(args[i]);
                if(Result == true)
                {
                    System.out.println( args[i] +" "+ p.toString()+ " " +GetASNName(p.len));
                }
            }
	    /*
	     * XXX:---------------------------------------------------------------------------------------------
	     * print something out if it was not matched
	     */

	        if (matched == 0)
            {
                System.out.println(p.getLen());
                System.out.println( args[i]+" : no prefix");
            }
        }
        return;
    }

    //Goes through file, adding all AS's to a list, which later used to retrive information
    public static void FetchASNames(String fileName)
    {
        try
        {
            BufferedReader file = new BufferedReader(new FileReader(fileName));
            String line;

            while((line = file.readLine()) != null)
            {
                ASNlist.add(line);
            }
            file.close();
        }
        catch(FileNotFoundException e)
        {
            System.err.println("could not open ASN file " + fileName);
            return;
        }
        catch(IOException e)
        {
            System.err.println("error reading ASN file " + fileName + ": " +e);
        }
    }

    //finds the AS Name by a AS Number
    private static String GetASNName(int p)
    {
        try
        {
            String ASN = ASNlist.get(p);
            return ASN;
        }
        catch(Exception x)
        {
            return "";
        }
    }
};