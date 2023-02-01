# Introduction

Twitter blocked my application on the platform, so the code is not quite finished, I give it as it is, suddenly someone will be interested.

But it workable.

# Registration in services

The registration process is divided between Pingen and Mastosync.

## Registration in Mastodon

![Mastodon diagram](http://www.plantuml.com/plantuml/png/bP7BRi8m44NNy1KZRxffHPvTLB4bNa98zA6GNTL5m0wuTUCeCQfGwR_NJeX299g25xPdhkVuUapv9e-pBSpWkmzwY7Z5X91hZ5QeIRpl2eMHECdW88Y6eyKKHi_XP1fYP24OJnUBEhIhSFao7C-VPaOcPN4zUa4KaY2Q44w3WLxCDlfYJB7yfW1bTb1B0ttqV74MvonLymR1ASW4gdH5kOLq9mGaCeTSIlKVyVC4fciOtwJmmVQW93p6XP9Kff39mfWxadboJ1IGIWNPRYLuiMKdtdjwDhqYf5zUYb99cUxNQzLAm822n28GQdy7Jh5zgD-5pGq7xGdhVXRRE6FcSCxMCgS8tXgQVOR6Ch4tEAV1sINBGBjnJWVElHxdTk2y1kTUYFFEuShFDa3_Ug3VuU--mVaNuYpqa5XmH-glqT5VejCMdPReTebUM_HQeb-AFxy0)

## Registering on Twitter

![Twitter diagram](http://www.plantuml.com/plantuml/png/bL8zJyD03DtpAwnE88M4hWnK81Oae9LA1wRebBZfYVOSxfn6yEjnFYvSH9OkKHA_z_pyRDPyH7XlD3DQg9qyLCq7ilf7iIO33hzgD3b6kxPsSm-lkhIAoS8TbCWZbUaJJJGM1RZHfaGZi5HLEZqlK-wXgT0UfCVE7gC14wBNRMsPt2hOwj8ehYq6CDzELAarcjhMd-EiwmzIFGGARbJmlB1J9p0GzpHCB_uDWyfpT2xe6qmjqoJ3A4bEfy67JmrNWUmaZox0o-W-yqTBJSzUL-Bd4WbF_JFmGgKsKAaIP_IxuWpfYU2RQcZQS9Z66q6VfgggrOpS1xePiStp6-HKpDxKM0kZ-xVrVZ_zYCEuPxHwhz5UUONhECmXE82WMxVot6BX9kSGCZE70k2uXMQwY9ryoL-ZJbEF2oSl4kf2PBVgZBy0)

## Authorized request on Twitter

![Twitter request](http://www.plantuml.com/plantuml/png/RP112y8m38Nl-nN1irftzI285o-Yg4TXb2vqnT7EDbFmrvkvubhmblQzl2y96MeHjZqrpfitrk3nqMCW80VNbo52NnoCzfASXyK6VlKQpgCpasU12wbnnhf6JXaZG7cKTIWcEOy2gHI6K96xevrdhoeWD-UKax4QBVS9sHdnB4N424OEg-mPnb3-R439jeKQW_Acu_QRJSF_ac9woZaEgULo9U5Yj9NKUqnioIAJCR2HpzbyHyccZsTbzswOBHxy0m00)

# User roles

* The main one, scope read, posts in Mastodon are read from him and published on his behalf on Twitter
* Optional, scope write, posts with responses from Twitter are published as responses from this user
in Mastodon to the posts of the main.

Of particular note is the inconsistency of the threads in Mastodon. If there is a conversation between two other users in your thread,
which do not mention you and you do not have subscriptions to them.  Then you will not receive any notifications about what is in your thread
there is some activity going on.  Moreover, even the server on which you created the user may not receive notifications about this.
or any information whatsoever, since only if there are subscriptions do they come.
