 I've noticed some differences the way design is coded here, w.r.t. what I've seen in actual product designs...
In a way this coding style is better, but on a flip side, it seems to be a bit complicated.

The difference is, that the code seems to be spread across the files, and it's a bit tricky to find where things are defined.
I think once you know where things are, it's not a problem at all, but for a beginner it's a little bit hard to figure out.

In actual product designs, one block contains very much everything within the block(except for macros), but this one may not, especially when the interface definition is included other block's codes.
one suggestion would be, the interface should be defined in one file, so that we can just look into that single file to search interface definitions, just like a macro definition files. Of course we can just simply grep the keywords in all files, but this is cumbersome, hence one single file would be a good idea, IMO.

Also, some codings such as what I questioned last Sat., I think they are more like software oriented coding style, and fortunately I don't see that many of them in other codes(there are some more, but not many as I initially anticipated; It was rather easy as I go thru).  So overall the codes are fairly straightforward to read thru...