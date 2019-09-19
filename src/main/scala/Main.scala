import Main.system
import akka.actor.ActorSystem
import akka.actor.FSM.Failure
import akka.actor.Status.Success
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes

import scala.concurrent.{Await, ExecutionContext, Future}


object Main extends App{
  val host= "0.0.0.0"
  val port=9090
  val elements=HealthAdvice.generateSomeHealtAvices
  //required for akka, streams,actors
  implicit val system: ActorSystem = ActorSystem("CrudAkka")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher
  import akka.http.scaladsl.server.Directives._
  def route= path("advice") {
    get{
      complete((StatusCodes.Accepted,HealthAdvice.getOneRandomAdvice(elements).toString))
    }
  }
  val binding: Future[Http.ServerBinding]= Http().bindAndHandle(route,host,port)
  import scala.concurrent.duration._

  binding.onComplete {
    case util.Failure(exception) => println(s"get the error ${exception.getMessage}")
    case util.Success(value) => println(s"success running http://$host:$port")
  }
  Await.result(binding,3.seconds)
}
