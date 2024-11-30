cmake_minimum_required(VERSION 3.10.2)

# プロジェクト名を指定
project("coin")

# NDKで必要なC++標準を指定
set(CMAKE_CXX_STANDARD 14)

# ソースファイルを指定
add_library(
        native-lib  # ライブラリ名
        SHARED     # 動的ライブラリとしてビルド
        src/main/cpp/coin.cpp  # C++のソースコードファイル
)

# Androidの標準ライブラリをリンク
find_library(
        log-lib
        log
)

# ネイティブライブラリをリンク
target_link_libraries(
        native-lib
        ${log-lib}
)