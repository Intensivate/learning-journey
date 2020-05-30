One thing we'd like to discuss is the progression of Sodor
Sodor's value is being training wheels -- it is simple compared to Rocket
So, one idea is to have a progression of Sodor versions
Start with dead simple version, that has flat memory arrays that are loaded from hex files.
And have a selection of CPU implementations, from micro-coded up to 5 stage
Then, create a second dimension of progression.
at the system level.
So, first add real caches.
then add tilelink between caches, with snooping
then add atomic instr support 
A third dimension is code complexity -- Rocket uses cake pattern and it uses sophisticated parameterization system 
It would be awesome to have a progression in that direction, as well.

Maybe just take one part of sodor, and add the cake pattern to it, showing the hierarchy that piles on top of the end-point.

It may be too advanced to fit with the goal of easy entry. Parameterization feature may be more helpful if it was only introduced in a more advanced version of Sodor..?

Let's see..  the important things are that code changes in one part get pulled into the other parts..
so, different branches works for that aspect..

Another important thing is mental model for beginners.

If we can figure out a way to make it simple for newbies to keep their head straight about which version of the code they're looking at. For example, some script that does all the cloning..  and it make several directories, and checks out a different branch in each directory. That way, a person has visual and spatial way to progress between versions.

one negative point is that if a bug is found in one that could lead to changes in other branches and it might become difficult to maintain all of them

The thing is about getting lost..  I, myself, find it confusing often, losing track of which version I have checked out..
Right..  the branch idea is good because we can merge bug fixes into the other branches.

But branches are easy to get lost in, and easy to get confusing..  losing track of which one is currently checked out.