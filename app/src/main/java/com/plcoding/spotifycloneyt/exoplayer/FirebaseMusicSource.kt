package com.plcoding.spotifycloneyt.exoplayer

import com.plcoding.spotifycloneyt.exoplayer.State.*

//firebase에서 가져온 미디어들을 우리가 실행할 수 있도록 해야함.
class FirebaseMusicSource {
    //firebase에서 데이터 가져올 때 시간이 좀 걸림. 여기서 음악 소스 초기화(로드완료) 되었는지 여부를 확인
    //이 상태에 따라서 동작을 설정해 주어야 하기 때문에 모든 상태를 enum class를 만들어서 상태에 따른 처리하도록 함.

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    private var state:State = State.STATE_CREATED
        set(value){
            if(value == STATE_INITALIZED || value == STATE_ERROR){ // 성공하든 에러발생하든 어쨋든 로드는 끝남
                synchronized(onReadyListeners){
                    field = value // field == state(변수) 이고 value 는 새 값
                    onReadyListeners.forEach { listener->
                        listener(state == STATE_INITALIZED) //음악소스를 성공적으로 초기화
                    }
                }
            }else{
                field = value
            }
        }

    fun whenReady(action : (Boolean) -> Unit):Boolean{
        if(state == STATE_CREATED || state == STATE_INITIALIZING){ // 음원 소스가 완전히 로드되지 않은 경우에는 이 액션을 나중에 실행되도록 예약
            onReadyListeners += action
            return false
        }else{ //초기화되거나 에러 발생하면 준비된 것.
            action(state == STATE_INITALIZED)
            return true
        }
    }


}
//음악의 로드 상태
enum class State{
    STATE_CREATED,
    STATE_INITIALIZING, // 다운로드 도중
    STATE_INITALIZED, // 다운로드 이후
    STATE_ERROR
}