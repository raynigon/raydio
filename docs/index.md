# Rayd.io

## What is Rayd.io?
Rayd.io is a simple, free and open source web radio client for your Raspberry Pi or PC.<br />
It allows you to extend your hi-fi set with a web radio.
Connect a Raspberry Pi to your hi-fi set, install Rayd.io on it and control everything with your smartphone.
<center>
<img src="/img/illustration/raydio-raspberry.png" height="250" alt="Rayd.io plus Raspberry Pi">
</center>

Rayd.io offers a simple web based user interface, which can be installed as a web application on iOS devices.

## How does it work?
Rayd.io can be installed on any Linux device[^1].
It provides a web based user interface on port 8080[^2],
which can be accessed with a web browser.
In this UI the user can add or remove radio stations,
and play one of the available radio stations.
Every user has the opportunity to download a list of preconfigured radio stations,
but there is no need to, since everything can be configured locally.

## Is it Open Source?
Rayd.io is a free and open source web radio client, which is currently available for linux.
Its designed with privacy first in mind.
Therefore no connection to a rayd.io server is needed.
You can even use rayd.io without an internet connection[^3].
If you decided to use functionality which is provided by the rayd.io servers,
no user specific data gets collected.
<br />
Rayd.io is licensed under the [Apache License 2.0](https://github.com/raynigon/raydio/blob/main/LICENSE.md).

[^1]: Any Linux Device which has JVM Support.
[^2]: Default Port is 8080, but it can be configured.
[^3]: If you have a locally available raydio stream, which works without internet connection.