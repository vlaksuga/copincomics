package com.copincomics.copinapp.data

data class GetMe(
    val head: HeaderContext,
    val body: BodyGetMe
)

data class BodyGetMe(
    val nick: String,
    val isadult: String,
    val c: String,
    val t: String,
    val d: String,
    val v: String,
    val kind: String,
    val type: String,
    val profileimg: String,
    val email: String,
    val coin: String,
    val apkey: String
)

//{
//    "head": {
//    "status": "ok"
//},
//    "body": {
//    "nick": "서현",
//    "isadult": "N",
//    "c": "eV9CGAmLTTGmgNQ2tHdijD:APA91bEWydRfmTCtvGvsby8tCVSC4w_Z167rkGc-KlvWsmP33A9W4wou3Dx22vjKfAmRkXy-rGxlX5Q9PqVko3j_sLBPvpZxEbSkVM7BV1Un0xRUw6Dng-q7ULnj-YWGZ8pVDdtsQvXB",
//    "t": "44fa2c60-a4e1-4445-9162-127ef86e732c",
//    "d": "android",
//    "v": "1.0",
//    "kind": "PC",
//    "type": "google.com",
//    "profileimg": "https://img82.com/bwVm3261ee09f78e346feb795e860603297e1.png",
//    "email": "",
//    "coin": "0"
//}
//}






