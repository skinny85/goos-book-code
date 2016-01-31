"Growing Object-Oriented Software, Guided by Tests" code walkthrough
====================================================================

I've written a blog article introducing this project in more detail
[here](http://endoflineblog.com/recreating-the-code-from-the-goos-book-example-project).

While reading the book ["Growing Object-Oriented Software, Guided by
Tests"](http://www.amazon.com/dp/0321503627) (awesome book, by the way -
highly recommend it), I had trouble keeping up with the example
project being tested and coded in Part III. To help my understanding,
I created this project, in which I copied the code in the book, which
made following the authors' train of thought much easier. I did it in
a gradual fashion, doing a commit after each change, and so the history
of this repo mirrors the structure of the book.

For code that was only mentioned, but not actually written in the book
(there is a surprisingly large amount of it), I used the official code
repository for the book located [here](https://github.com/sf105/goos-code).

I tried to stay as close to the book as I could, but there might be small
differences because of the aforementioned unlisted code - things like
formatting, order of fields/methods, some names etc. I also used Gradle as
the build system instead of Ant.

Remember that for the end-to-end tests to pass, you need a running XMPP
server listening on localhost:9090 (the book authors used Openfire version
3.6, and so did I - all you need to do is download and unzip it, and then run
`java -jar lib/startup.jar`) with the following users created:

Username           | Password
-------------------|---------
sniper             | sniper
auction-item-54321 | auction
auction-item-65432 | auction

You also need to set the 'Resource Policy' to 'Never kick'.
