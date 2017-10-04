TopMod
======

A REST interface to Topic Modeling.

|stars| |pulls| |autom| |build|

.. |stars| image:: https://img.shields.io/docker/stars/huygensing/topmod.svg
   :target: https://hub.docker.com/r/huygensing/topmod/
.. |pulls| image:: https://img.shields.io/docker/pulls/huygensing/topmod.svg
   :target: https://hub.docker.com/r/huygensing/topmod/
.. |autom| image:: https://img.shields.io/docker/automated/huygensing/topmod.svg
   :target: https://hub.docker.com/r/huygensing/topmod/
.. |build| image:: https://img.shields.io/docker/build/huygensing/topmod.svg
   :target: https://hub.docker.com/r/huygensing/topmod/builds/

Usage
-----

Edit ``config.yml`` as needed. Then compile and run TopMod
itself::

  mvn clean package &&
    ./target/appassembler/bin/topmod server config.yml

or use the Dockerfile, ``docker run -p 8080:8080 $(docker build -q .)``.

Alternatively, you can pull the latest build from Dockerhub::

  docker pull huygensing/topmod

and run that version, ``docker run -p 8080:8080 huygensing/topmod``.
This version also exposes various docker hub build properties at `/about`::

  curl -s localhost:8080/about | jq .

One demo endpoint exists::

  curl -H "Content-Type: application/json" \
    http://localhost:8080/suggest -d 'keys to look for'
