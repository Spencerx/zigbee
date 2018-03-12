# Overview

This project aims to provide a ZigBee Cluster Library written in Java.
Packages are broken down to provide the core ZigBee framework and separate projects for dongles.
Additionally, an Eclipse SmartHome compatible Zigbee binding can be build from those projects.

# Packages

## ZigBee Stack

## Dongles
  
### Texas Instruments CC2531

The library supports the Texas Instruments ZNP protocol over a serial interface.

### Silicon Labs Ember EM35x

The library supports the Silicon Labs EZSP protocol using ASH over a serial interface.

It is worth noting that EM3588 devices that have an embedded USB core will likely work with any baud rate, where dongles using external USB interface (eg CP2102 used with an EM3581) will likely require a specific baud rate. This has been noted on the HUSBZB-1 which embeds an EM3581 and requires a rate of 57600.

### Telegesis ETRX3

The library supports the Telegesis AT protocol over a serial interface. Currently implemented against R309. Note that some dongles such as the Qivicon Funkstick may use PID/VID codes that require special drivers or they will not be detected as a serial port.

Under Linux you can add the Qivicon dongle without a custom made kernel with the following command -:

``` 
echo 10c4 89fb > /sys/bus/usb-serial/drivers/cp210x/new_id
```

### XBee

The XBee S2C XStick is supported.

## Tested Hardware
 
## ZigBee Dongles and Chipsets

The following table provides a summary of some of the dongles / chipsets that are available on the market nd their support within the library. Receive sensitivity and transmit power are the main parameters affecting RF performance - it should be noted that regulations may reduce transmit power in some areas of the world and other factors can also impact performance. 
 
| Model                 | Support         | Receive     | Transmit     | Antenna  |
|-----------------------|-----------------|-------------|--------------|----------|
| Xbee XU-Z11           | Yes             | -90dBm      | +4.5dBm      | Internal |
| EM358                 | Yes (EZSP)      | -100dBm     | +8.0dBm      | Internal |
| **EM358LR**           | Yes (EZSP)      | -103dBm     | **+20.0dBm** | Internal |
| MGM111                | Yes (EZSP)      | -99dBm      | +10dBm       | Internal |
| RaspBee               | Yes (CONBEE)    | **-105dBm** | +8.7dBm      | Internal |
| ConBee                | Yes (CONBEE)    | **-105dBm** | +8.7dBm      | Internal |
| CC2530                | Yes (ZNP)       | -97dBm      | +4.5dBm      |          |
| CC2531                | Yes (ZNP)       | -97dBm      | +4.5dBm      |          |
| CC2538                | Yes (ZNP)       | -97dBm      | +7.0dBm      |          |
| CC2650                | Yes (ZNP)       | -100dBm     | +5.0dBm      |          |
| ATSAMR21              | No              | -99dBm      | +4.0dBm      |          |
| JN5169                | No              | -96dBm      | +10.0dBm     |          |
| HUSBZB-1              | Yes (EZSP)      |             |              | Internal |
| Telegesis ETRX3       | Yes (Telegesis) |             |              | Internal |
| Qivicon Funkstick     | Yes (Telegesis) |             |              | Internal |

* Receive: Defines the typical receive performance. A smaller number is best.
* Transmit: Defines the maximum output power. A larger number is best.

# Contributing

* Code style should use [standard naming conventions](https://www.thoughtco.com/using-java-naming-conventions-2034199)
* Please consider raising issues before working on an enhancement to provide some coordination.
* Keep PRs short - try and keep a single PR per enhancement. This makes tracking and reviewing easier.
* Contributions must be supported with tests.
* Code must be formatted using the Eclipse code formatter provided in the project.
* Contributions must be your own and you must agree with the license.
 
## Maven goals
 
* To build the Zigbee artifacts: ```mvn clean install```
* To build the Eclipse SmartHome Zigbee Binding: ```mvn -f pom-esh.xml clean install```
* To format the license header: ```mvn license:format```

# License

The code is licensed under [Eclipse Public License](https://www.eclipse.org/legal/epl-v20.html).
