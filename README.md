# arrosage

Application générée de base aveec Jhipster 6.10.3, documentation :  [https://www.jhipster.tech/documentation-archive/v6.10.3](https://www.jhipster.tech/documentation-archive/v6.10.3).

## Installation de WiringPi

clone et build
```
sudo apt-get install make
sudo apt-get install gcc
sudo apt-get install git-core
git clone https://github.com/WiringPi/WiringPi.git
cd WiringPi
./build
```
Test Wiring pi

```
gpio readall
```
## Logiciel requis
Logiciel requis : 
- postgresql 
- utilisateur postgresql : arrosage / arrosage
- base de donnée arrosage appartenant à l'utilisateur arrosage

## Installation
### Compile JHipster application for Pi 
```
./mvnw package -Ppi -DskipTest
```

### Installation
- copy start.sh, stop.sh, target\arrosage.jar into install folder (/opt/soft/arrosage)
- copy arrosage in /etc/init.d
- execute : update-rc.d arrosage defaults
