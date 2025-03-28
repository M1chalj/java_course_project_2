# Stock Exchange System

*This project was created for the Object-Oriented Programming Course at the University of Warsaw, Faculty of Mathematics, Informatics, and Mechanics.*

*Polish description of the project is available in the `Treść.pdf` file.*

## Overview

A simulation of a stock exchange system where investors place buy and sell orders. Orders must specify:

- **Type**: Buy / Sell
- **Stock ID**: Up to 5 uppercase letters
- **Shares**: Positive integer
- **Price Limit**: Positive integer

A transaction occurs when a buy order matches or exceeds a sell order’s price.

## Trading Rules

- Orders are matched by **best price** and then by **earliest submission**.
- Investors can specify order validity (Immediate, Open-Ended, Execute or Cancel, Expiring After N Turns).
- No short selling allowed—investors must have sufficient assets.

## Investor Strategies

- **RANDOM**: Places random orders.
- **SMA (Simple Moving Average)**: Uses SMA(5) & SMA(10) to trigger buy/sell decisions.

## Running the Simulation

Execute with an input file and number of turns:

```sh
java GPWSimulation input.txt 100000
```

### Input Format Example

```
# Investors: 4 RANDOM, 2 SMA
R R R R S S
# Stocks with last transaction prices
APL:145 MSFT:300 GOOGL:2700
# Initial portfolio for all investors
100000 APL:5 MSFT:15 GOOGL:3
```

### Output Example

```
100000 APL:5 MSFT:15 GOOGL:3
...
```
