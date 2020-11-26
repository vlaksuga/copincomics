package com.copincomics.copinapp.data


data class MyQuestions(
    val head: HeaderContext,
    val body: BodylistMyQuestion
)

data class BodylistMyQuestion(
    val list: List<MyQuestion>
)


data class MyQuestion(
    val txt: String,
    val accountpkey: String,
    val qnapkey: String,
    val answer: String,
    val kind: String,
    val answerdate: String,
    val title: String,
    val issuedate: String,
    val status: String,
)

//"txt": "text ddd",
//"accountpkey": "238",
//"qnapkey": "12",
//"kind": "tx",
//"answer": " 1111frefre43fer   ghgfh            ",
//"answerdate": "99991231000000",
//"title": "title test",
//"issuedate": "20201111101900",
//"status": "Q"


data class EmptyReturn(
    val head: HeaderContext,
    val body: EmptyBody
)

data class EmptyBody(
    val fake: String
)


data class ChangeNickReturn(
    val head: HeaderContext,
    val body: ChangeNickBody
)

data class ChangeNickBody(
    val nick: String,
    val c: String,
    val t: String,
    val d: String,
    val v: String,
    val profileimage: String,
)
