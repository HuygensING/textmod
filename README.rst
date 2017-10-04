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

One demo endpoint exists::

  curl -H "Content-Type: application/json" \
    http://localhost:8080/suggest -d '["keys", "to", "look", "for"]'
