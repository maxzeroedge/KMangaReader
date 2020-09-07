import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.shape.Line
import javafx.stage.Stage

class App: Application() {
    fun main(args: Array<String>) {
        launch("")
    }

    override fun start(primaryStage: Stage?) {
        val root = Group()
        val list = root.children
        val line = Line()
        line.startX = 0.0
        line.startY = 0.0
        line.endX = 100.0
        line.endY = 100.0
        list.add(line)

        val scene = Scene(root, 1024.0, 768.0)

        primaryStage!!.title = "Line Tutorial"
        primaryStage.scene = scene
        primaryStage.isFullScreen = true
        primaryStage.show()
    }
}

fun main(args: Array<String>) {
    App().main(args)
}