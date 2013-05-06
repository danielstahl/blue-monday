import com.twitter.finatra.{View, FinatraServer, Controller}

class WebController extends Controller {
  get("/") {
    request =>
      render.plain("Hello world").toFuture
  }

  case class StaticView(resource:String) extends View {
    val template = resource
  }

  get("/static/:resource") {
    request =>
      render.view(StaticView(request.routeParams.getOrElse("resource", "index.html"))).toFuture
  }
}

object WebServer {
  val controller = new WebController

  def main(args: Array[String]) = {
    FinatraServer.register(controller)
    FinatraServer.start()
  }
}
