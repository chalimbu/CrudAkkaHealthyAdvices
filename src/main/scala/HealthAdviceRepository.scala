import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

trait HealthAdviceRepository {
  def all(): Future[Seq[HealthAdvice]]
  def exercise(): Future[Seq[HealthAdvice]]
  def alimentation(): Future[Seq[HealthAdvice]]
  def getOneRandomAdvice(): Future[HealthAdvice]
}

class InMemoryHealthRepository(val HealthAdvices: Seq[HealthAdvice]=Nil)(implicit ec:ExecutionContext)
  extends HealthAdviceRepository{

  override def all(): Future[Seq[HealthAdvice]] = Future(HealthAdvices)

  override def exercise(): Future[Seq[HealthAdvice]] =
    Future(HealthAdvices.toList.filter(_.category.getOrElse("")=="Exercise"))

  override def alimentation(): Future[Seq[HealthAdvice]] =
    Future(HealthAdvices.toList.filter(_.category.getOrElse("")=="Alimentation"))

  def getOneRandomAdvice={
    Future.successful{
      val r = new Random()
      val adviceNumber = (r.nextDouble() * this.HealthAdvices.size).toInt
      HealthAdvices(adviceNumber)
    }
  }
}