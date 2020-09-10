import com.palashmax.mangareader.MangaReader
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.FlowPane
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage
import kotlinx.coroutines.*

class App: Application() {
    val mangaReader = MangaReader()
    val WIDTH = 1024.0
    val HEIGHT = 768.0
    lateinit var root: Group

    fun main(args: Array<String>) {
        launch("")
    }

    override fun start(primaryStage: Stage?) {
        root = Group()
        val scene = Scene(root, WIDTH, HEIGHT)

        this.root.children.add(this.showLoadingWidget())

        primaryStage!!.title = "KMangaReader"
        primaryStage.scene = scene
        // primaryStage.isFullScreen = true
        primaryStage.show()

        val titlesList = mangaReader.fetchTitles()
        val baseListView = getListView(titlesList, SelectionMode.SINGLE)
        this.root.children.clear()
        this.root.children.add(baseListView)

        val nextButton = getNextButton {
            val selection = baseListView.selectionModel.selectedIndices.first()
            baseListView.items.clear()
            titleMouseClicked(titlesList, selection)
        }
        this.root.children.add(nextButton)
    }

    private fun getNextButton(callback: () -> Unit): Button {
        val nextButton = Button()
        nextButton.text = "Next"
        nextButton.setOnMouseClicked {
            run {
                callback()
                // scene.root = child as Parent?
            }
        }
        nextButton.translateX = WIDTH - 50
        // button.translateY = HEIGHT*0.9
        return nextButton
    }

    private fun getListView(mapList: List<Map<String, String>>, selectionMode: SelectionMode = SelectionMode.SINGLE): ListView<String> {
        val baseListView = ListView<String>()
        for (mapItem in mapList){
            baseListView.items.add(mapItem["name"])
        }
        baseListView.translateY = 25.0
        baseListView.prefWidth = WIDTH
        baseListView.prefHeight = HEIGHT*0.9
        baseListView.selectionModel.selectionMode = selectionMode
        return baseListView
    }

    private fun showLoadingWidget(): Node {
        // TODO:
        val loadingText = Text("Loading...")
        loadingText.font = Font.font(20.0)
        loadingText.x = WIDTH / 2
        loadingText.y = HEIGHT / 2
        return loadingText
    }

    private fun titleMouseClicked(titlesList: List<Map<String, String>>, selectedIndex: Int){
        this.root.children.clear()
        this.root.children.add(this.showLoadingWidget())
        val titleMap = titlesList[selectedIndex]
        val chapters = titleMap["url"]?.let { mangaReader.fetchChapters(it) }

        val baseListView = getListView(chapters!!, SelectionMode.SINGLE)
        val nextButton = getNextButton {
            val selection = baseListView.selectionModel.selectedIndices.map { indx -> indx }
            baseListView.items.clear()
            chapterMouseClicked(chapters, selection)
        }
        this.root.children.clear()
        this.root.children.add(baseListView)
        this.root.children.add(nextButton)
    }

    private fun chapterMouseClicked(chapters: List<Map<String, String>>, selectedIndices: List<Int>) {
        // TODO: Show pages
        this.root.children.clear()
        this.root.children.add(this.showLoadingWidget())
        val chapter = chapters[selectedIndices[0]]
        val pages = mangaReader.fetchPageImages(chapter["url"] as String).mapIndexed { index, imageUrl -> mapOf(
                Pair("name", "Page: ${index+1}"),
                Pair("url", "https:$imageUrl")
        ) }

        val pane = FlowPane()
        pane.hgap = 25.0
        pages.parallelStream().forEach { page -> run{
            val image = Image(page["url"])
            val imageView = ImageView(image)
            imageView.fitHeight = 50.0
            imageView.fitWidth = 25.0
            pane.children.add(imageView)
            print("done ${page["name"]}" )
        } }

        this.root.children.clear()
        this.root.children.add(pane)
    }
}

fun main(args: Array<String>) {
    App().main(args)
}