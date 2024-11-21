package domainExpansion

typealias Point = Pair<Int, Int>

object Constant {
    const val SCREEN_HEIGHT = 1080;
    const val SCREEN_WIDTH = 1920;
    const val HEIGHT = 7;
    const val WIDTH = 7;
    const val CELL_SIZE = 140;
    const val TOP_PAD = (SCREEN_HEIGHT - HEIGHT * CELL_SIZE) / 2;
    const val LEFT_PAD = (SCREEN_WIDTH - WIDTH * CELL_SIZE) / 2;
    const val MAX_DISTANCE = 3;

    const val FIRST_TURN_MAX_TIME = 1000;
    const val TURN_MAX_TIME = 100;
    const val MAX_TURNS = 112;
}
