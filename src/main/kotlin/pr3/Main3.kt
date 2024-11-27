package pr3

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.toObservable

data class UserFriend(val userId: Int, val friendId: Int)

fun getFriends(userId: Int, userFriends: Array<UserFriend>): Observable<UserFriend> {
    return userFriends.filter { it.userId == userId }
        .toObservable()
}

fun main() {
    val userFriends = Array(1000) {
        UserFriend(
            userId = (0..1000).random(),
            friendId = (0..1000).random()
        )
    }

    val userIds = Array(5) { (10..100).random() }
    println(userIds.joinToString(" "))

    val userFriendsStream = userIds.toObservable()
        .flatMap { userId -> getFriends(userId, userFriends) }

    userFriendsStream.subscribe(
        { println(it) },
        { error -> println("Error: $error") },
        { println("Completed") }
    )
}
