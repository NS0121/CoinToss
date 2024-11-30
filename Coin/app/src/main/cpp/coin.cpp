#include <android/log.h>
#include <jni.h>
#include <random>

// std::省略
using std::random_device;
using std::mt19937;
using std::uniform_int_distribution;

// コイントスをテンプレート化
template <typename coinTossT>
coinTossT spawnRandomNumber(coinTossT low, coinTossT high)
{
    // 乱数を生成するための式を準備
    random_device rand;
    mt19937 engine(rand());
    // 乱数を生成
    uniform_int_distribution<coinTossT> dist(low, high);
    return dist(engine);
}


// JNI、JavaにC++を渡す際に必要な宣言
extern "C"
JNIEXPORT void  JNICALL
Java_com_example_coin_MainActivity_coinToss(JNIEnv *env, jobject, jintArray resultArray) {
    // エラーに対処する
    try{
        // jintはjavaのint型
        jint result = spawnRandomNumber<int>(0, 1);
        // Java側の配列に結果を渡す
        env->SetIntArrayRegion(resultArray, 0, 1, &result);
    } catch (...) {
        //エラーになったら際の処理
        __android_log_print(ANDROID_LOG_ERROR, "coinToss", "Error occurred during coin toss");
    }
}
