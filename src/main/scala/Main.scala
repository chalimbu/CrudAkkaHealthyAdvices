import Main.system
import akka.actor.ActorSystem
import akka.actor.FSM.Failure
import akka.actor.Status.Success
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import scala.util.{Success => SuccessF, Failure=>FailureF}
import scala.concurrent.duration._


import scala.concurrent.{Await, ExecutionContext, Future}


object Main extends App{
  val host= "0.0.0.0"
  val port=9090
  val elements=HealthAdvice.generateSomeHealtAvices
  val storage=new InMemoryHealthRepository(elements)
  //required for akka, streams,actors
  implicit val system: ActorSystem = ActorSystem("CrudAkka")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher
  import akka.http.scaladsl.server.Directives._
  def route= path("advice") {
    get{
      //val randomAdvice=storage.getOneRandomAdvice
      //randomAdvice.onComplete({
      //  case SuccessF(advice) => {
      //    complete((StatusCodes.Accepted,advice.toString))
      //  }
      //  case FailureF(exception) => {
      //    complete((StatusCodes.BadRequest,s"failure: ${exception.getMessage}"))
      //  }
      //})
      complete((StatusCodes.Accepted,storage.getOneRandomAdvice.toString))

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
