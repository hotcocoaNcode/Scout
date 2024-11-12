# Scout
> *Become the NSA of your friend group.*
---
Scout is a Fun Little Discord Bot™ that half-asses a word2vec implementation (not actually word2vec at all, but close enough that you can get the Funny Graphs out of it) so you can track your friends' speech patterns and tendencies on the internet.
## Why would you ever do this?
For fun.
## Are you sociopathic?
Maybe. *\*evil grin**
## How does it work?
It's *lightly inspired* by the CBOW (continuous bag of words) method from word2vec. Essentially messages are broken up into idea groups (by punctuation) and each occurrence of a word in that group is added to a counter per word. That's probably a horrible way of explaining it, so basically;

`Snow is pretty cool, but I like the rain.`

Gets broken up into

`["Snow is pretty cool", "but I like the rain"]`

And then for each word in those sentences, we count the occurrence of each other word. Let's take "snow" in the first one.

`snow; snow:1 is:1 pretty:1 cool:1`

Simple, right? But we also have to take into account the other words in the user's known vocabulary.
So our actual vector for this word, after processing is complete, looks somewhat like this:

`snow; snow:1 is:1 pretty:1 cool:1 but:0 i:0 like:0 the:0 rain:0`

And over time, as that user sends more messages, we add to their vocabulary and their occurrence counts for each word.

Occurrence with other words tends to lean towards meaning something similar. For example if somebody preaches about how they love their sister multiple times there will be a lot of occurrences for "love" and "sister" under both of those words.

This also means each word can be represented as a point in *n*-dimensional space, where *n* is the total word count. After applying that we can normalize its "position" vector (as to not bias meaning groupings towards how often words occur in data) throw it through something like a k-means clustering to identify what words in their vocabulary represent concepts.

## Okay, so why might this mean anything?

Well, a lot of the time people subconsciously use certain words while talking about certain people depending on how they feel about that person. These words might not have connotations in *your* head, but they're certainly very real to the other person and can be used to infer things about them. 

In theory if you know somebody subconsciously uses certain language to describe or talk with people they're attracted to, you can predict if they start to form relationships with other people in the server.

However, my source for that is I made it up and it's likely BS. I haven't tested this method yet so we'll see how it goes and what I learn.

## Important notes

This project is likely highly immoral. Don't deploy this if you haven't asked your friends or people in the server if it's okay. It's also likely motivated by my lack of understanding other people's emotions, and my therapist thinks I'm autistic, so take from that what you will.

Also this is really bad code I wrote it in like 2 hours on a school night to procrastinate spanish homework lol don't try to learn anything from this