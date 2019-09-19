import Main.system
import akka.actor.ActorSystem
import akka.actor.FSM.Failure
import akka.actor.Status.Success
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import scala.util.{Success => SuccessF, Failure=>FailureF}
import scala.concurrent.duration._
//for json support
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._


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
  val healthRouter=new HealthRouter(storage)
  val server=new Server(healthRouter.route,port,host)
  val binding: Future[Http.ServerBinding]=server.bind
  import scala.concurrent.duration._
  println(elements(1).category.getOrElse("sd"))
  binding.onComplete {
    case util.Failure(exception) => println(s"get the error ${exception.getMessage}")
    case util.Success(value) => println(s"success running http://$host:$port")
  }
  Await.result(binding,1.seconds)
}
