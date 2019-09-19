import Main.{host, port}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContext, Future}
//implict akka http dependencies
class Server(router: Route,port: Int,host: String)(implicit system: ActorSystem,ex: ExecutionContext,mat: ActorMaterializer){
  def bind: Future[ServerBinding]=Http().bindAndHandle(router,host,port)
}
