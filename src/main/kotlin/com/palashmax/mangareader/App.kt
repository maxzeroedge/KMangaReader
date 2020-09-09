import com.palashmax.mangareader.MangaReader
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.stage.Stage

class App: Application() {
    val mangaReader = MangaReader()
    val WIDTH = 1024.0
    val HEIGHT = 768.0

    fun main(args: Array<String>) {
        launch("")
    }

    override fun start(primaryStage: Stage?) {
        val root = Group()
        val scene = Scene(root, WIDTH, HEIGHT)

        val rootGroupChildren = root.children

        val titlesList = mangaReader.fetchTitles()

        val listView = ListView<String>()
        for (title in titlesList){
            listView.items.add(title["name"])
        }
        listView.prefWidth = WIDTH
        listView.prefHeight = HEIGHT*0.9
        listView.selectionModel.selectionMode = SelectionMode.SINGLE

        rootGroupChildren.add(listView)

        val button = Button()
        button.text = "Next"
        button.setOnMouseClicked {
            run {
                val title = titlesList[listView.selectionModel.selectedIndices.first()]
                val child = this.navigateToChapters(title)
                scene.root = child as Parent?
            }
        }
        button.translateX = WIDTH - 100
        button.translateY = HEIGHT*0.9
        rootGroupChildren.add(button)

        primaryStage!!.title = "Line Tutorial"
        primaryStage.scene = scene
        // primaryStage.isFullScreen = true
        primaryStage.show()
    }

    fun navigateToChapters(titleMap: Map<String, String>): Node{
        val chapters = titleMap["url"]?.let { mangaReader.fetchChapters(it) }
        val listView = ListView<String>()
        if (chapters != null) {
            for (title in chapters){
                listView.items.add(title["name"])
            }
        }
        listView.prefWidth = WIDTH
        listView.prefHeight = HEIGHT*0.9
        listView.selectionModel.selectionMode = SelectionMode.MULTIPLE

        val button = Button()
        button.text = "Next"
        button.translateX = WIDTH - 100
        button.translateY = HEIGHT*0.9

        val root = Group()
        root.children.add(listView)
        root.children.add(button)
        return root
    }
}

fun main(args: Array<String>) {
    App().main(args)
}