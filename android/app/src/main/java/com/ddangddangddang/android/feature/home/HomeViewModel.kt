package com.ddangddangddang.android.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.model.AuctionHomeStatusModel

class HomeViewModel : ViewModel() {
    val auctions: List<AuctionHomeModel> = listOf(
        AuctionHomeModel(
            1,
            "[히트다 히트!] 동물의 숲 에디션 닌텐도 스위치",
            "https://image.homeplus.kr/td/9edd7928-970f-4b5b-adb3-10812e12cf20",
            26000000,
            AuctionHomeStatusModel.SUCCESS,
            26,
        ),
        AuctionHomeModel(
            2,
            "M1 맥북 에어",
            "https://blog.wishket.com/wp-content/uploads/2021/03/2-23.jpg",
            400000,
            AuctionHomeStatusModel.ONGOING,
            100,
        ),
        AuctionHomeModel(
            3,
            "COVERNAT 제딱이 유니폼",
            "https://m.vintagecrown.co.kr/web/product/big/202207/25483.jpg",
            100000,
            AuctionHomeStatusModel.UNBIDDEN,
            0,
        ),
        AuctionHomeModel(
            4,
            "하트 모양 복숭아",
            "https://webp2.xplant.co.kr/data/item/16882/1688259853_l1.jpg",
            5000000,
            AuctionHomeStatusModel.FAILURE,
            0,
        ),
        AuctionHomeModel(
            5,
            "앙 귀요미 글로띠",
            "https://i.namu.wiki/i/y7qTOOIL6nIa2cXybk511OASqwAGMgZiNjh6CtErz0ust7MPJaztzSYiypYevehQOjdJc-TQvTctUk7N629V7A.webp",
            90000000,
            AuctionHomeStatusModel.ONGOING,
            5000,
        ),
        AuctionHomeModel(
            6,
            "고릴라 모양 치토스",
            "https://image.ytn.co.kr/general/jpg/2017/0209/201702091450067512_img_0.jpg",
            10000000,
            AuctionHomeStatusModel.SUCCESS,
            2000,
        ),
    )

    init {
        Log.d("glo", "view model created!!")
    }
}
