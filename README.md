# LikeBalance

It's a app for search your likecoin balabce, and here is [google play](https://play.google.com/store/apps/details?id=com.noahliu.likebalance).

![](https://github.com/thumbb13555/LikeBalance/blob/master/dfb576de-6ee3-43e6-bc80-17da46e33012.png)

## Preview

![](https://github.com/thumbb13555/LikeBalance/blob/master/MainUI.png)
![](https://github.com/thumbb13555/LikeBalance/blob/master/show.gif)

## Quick Start
```
java
  |
  |--Controller
  |    |-BalanceProvider.kt
  |    |-LikePriceProvider.kt
  |    |-LoginActivity.kt
  |    |-MainActivity.kt
  |
  |--Module
  |
  |--Untils
      |-API.kt
  
```
### ./Controller

```
Controller
   |-BalanceProvider.kt
   |-LikePriceProvider.kt
   |-LoginActivity.kt
   |-MainActivity.kt
```

**BalanceProvider** Is where control the Likecoin balance provider, and service is in **../Module/Service/BalanceService.kt**

**LikePriceProvider** Is where show the Likecoin change to currency price, and service is in **../Module/Service/PriceSerivce.kt**

**LoginActivity** If client wants to sign in by matter account, here it is.

**MainActivity** The main interface, where can bind your account.

## Contributing

If you want to pull a new or report bug, you can connect me by [Matters](https://matters.news/@thumbb13555) or email:[thumbb13555@gmail.com](thumbb13555@gmail.com)


## License
The module is available as open source under the terms of the [MIT License](https://opensource.org/licenses/MIT).

