import java.util.*;
import java.io.*;
//http://www.tutorialspoint.com/java/java_using_comparator.htm
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
            System.out.println("a has a greater prefix than b : a:" + a.len + " b : " + b.len);
            return 1;
        }
        else if(a.len < b.len)
        {
            System.out.println("a has a smaller prefix than b : a:" + a.len + " b : " + b.len);
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
    public int         len;
    public String      asn;

    public prefix(String net, int len, String asn)
    {
	/*------------------------------------------------------------------------
	 * XXX:
	 * initialise the object given the inputs.  break
	 * the network ID into four integers.
	 */
	  String arrayString[] = net.split(".");
        this.net[0] = Integer.parseInt(arrayString[0]);
        this.net[1] = Integer.parseInt(arrayString[1]);
        this.net[2] = Integer.parseInt(arrayString[2]);
        this.net[3] = Integer.parseInt(arrayString[3]);

        this.asn = asn;
        this.len = len;

    }

    public String toString()
    {
	/* pretty print out of the prefix! my lecturer is kind! */
        return net[0] + "." + net[1] + "." + net[2] + "." + net[3] + "/" + len;
    }

    /*
     * match
     *
     * given an address, determine if it is found in this
     * prefix structure or not.
     */
    public boolean match(String addr)
    {
        boolean Result = false;
        int[] mask = {0x80, 0xC0, 0xE0, 0xF0, 0xF8, 0xFC, 0xFE, 0xFF};
        /*
	     * XXX:------------------------------------------------------------------------
	     * break up the address passed in as a string
	     */
        String[] Stringarray = addr.split(".");
        int[] array = {0,0,0,0};

        array[0] = Integer.parseInt(Stringarray[0]);
        array[1] = Integer.parseInt(Stringarray[1]);
        array[2] = Integer.parseInt(Stringarray[2]);
        array[3] = Integer.parseInt(Stringarray[3]);

        for(int i=0; i<4; i++)
        {

	    /*
	     * XXX:
	     * compare up to four different values in the dotted quad,
	     * (i.e. enough to cover this.len) to determine if this
	     * address is a match or not
	     */
        }
        return Result;
    }
};

class ip2as
{
    protected static ArrayList<String> ASNlist = new ArrayList<>();
    public static void main(String args[])
    {
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
        for(int i=2; i<args.length; i++)
        {
            int matched = 0;

	    /*
	     * x contains the sorted array of prefixes, organised longest
	     * to shortest prefix match
	     */
            for(int j=0; j<x.length; j++)
            {
                prefix p = x[j];

		/*-------------------------------------------------------------------------
		 * XXX:
		 * check if this prefix matches the IP address passed in----------------------------------------------
		 */
		        boolean Result = p.match(args[i]);
                if(Result == true)
                {
                    System.out.print(args[i]);
                    p.toString();
                    System.out.println(GetASNName(p.len));
                }
            }
	    /*
	     * XXX:---------------------------------------------------------------------------------------------
	     * print something out if it was not matched
	     */

	        if (matched == 0)
            {
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