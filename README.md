# COMP202-Prac3

READ PDF (03-ip2as.pdf)
**Longest Prefix Matching**

**1 Introduction**
This practical will reinforce concepts learned in the course around determining the longest matching
prex. You are supplied with some code that parses a le of prex to AS mappings derived from
publicly available BGP data.
You are required to complete the code so that it can do a longest prex match lookup for
addresses supplied on the command line, as well as print out the corresponding name of the AS.
The code is commented in the places where it needs to be completed. The reference implementation
is about 200 lines of code, and you are supplied with 100 of those lines.
Due to limitations of Java, the implementation you complete will be far from optimal. The
easiest way to implement the prex::match method is to break the address into four integers,
and then mask the appropriate number of bits from each address integer and compare with the
corresponding network address integer. I have supplied you an array of mask values for this
purpose. This is roughly how you approach answering test questions on which routing entry is
chosen, so should be reasonably natural to you.

**2 Academic Integrity**
The les you submit must be your own work. You may discuss the assignment with others but
the actual code you submit must be your own. You must fully also understand your code and be
capable of reproducing and modifying it. If there is anything in your code that you don't under-
stand, seek help until you do.
You must submit your les to Moodle in order to receive any marks recorded on your veri-
cation page.

This assignment is due September 12th at 11am and worth 6% of your nal grade. If the as-
signment is veried after the 12th, it is worth 1% less for each lab session after the 12th where it
could have been veried.

**3 Materials**
You are provided with an IP2AS mapping le that was derived from public BGP data.
Briefly, I processed archives of BGP raw data les that contained routes (prexes and paths),
i.e.:

3303|7575|38022|681 130.217.0.0/16
2914|7575|38022|681 130.217.0.0/16
62567|2914|7575|38022|681 130.217.0.0/16

and extracted the unique origin ASes belonging to each prex. For example, in the above example,
the origin AS (the last AS in the path, likely to be the AS that announced the route) is 681. I
condensed the raw paths into a le where each line contains a unique prex and all origin ASes
associated with the prex. Where there is more than one origin AS, ASes are separated with an
underscore character.

130.217.0.0/17 681
130.217.0.0/16 681
130.217.128.0/17 681
192.107.171.0/24 681
192.107.172.0/24 681
205.204.15.0/24 3356_3549

You are also supplied with an asnames le, which associates an AS number with a name.

1 LVLT-1 - Level 3 Communications, Inc.,US
2 UDEL-DCN - University of Delaware,US
3 MIT-GATEWAYS - Massachusetts Institute of Technology,US
The format is the ASN, followed by free-form text naming or describing the ASN.

**4 Requirements**
1. Do not load any prex less specic than a /8, or more specic than a /24.
2. Ensure you handle the case where there is no prex covering an IP address (i.e. no mapping
from an IP to an ASN).
3. Ensure you handle the case where there is no name for a given ASN.

**5 Correctness**
If your program produces output like the following, you've probably implemented it correctly for
answering the verication questions.

$ java ip2as ../20150701.ip2as ../asnames.txt 128.30.2.155 10.110.8.71 1.0.192.1 205.204.15.1
128.30.2.155 128.30.0.0/15 3 MIT-GATEWAYS - Massachusetts Institute of Technology,US
10.110.8.71 : no prefix
1.0.192.1 1.0.192.0/21 23969 TOT-NET TOT Public Company Limited,TH
205.204.15.1 205.204.15.0/24 3356 LEVEL3 - Level 3 Communications, Inc.,US
205.204.15.1 205.204.15.0/24 3549 LVLT-3549 - Level 3 Communications, Inc.,US
