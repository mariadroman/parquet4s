package com.github.mjakubowski84.parquet4s.pekko

import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.scaladsl.{Sink, Source}
import com.github.mjakubowski84.parquet4s.{ParquetStreams, Path}

import java.nio.file.Files
import scala.util.Random

object WriteAndReadPekkoApp extends App {

  case class Data(id: Int, text: String)

  val count = 100
  val data  = (1 to count).map(i => Data(id = i, text = Random.nextString(4)))
  val path  = Path(Files.createTempDirectory("example"))

  implicit val system: ActorSystem = ActorSystem()
  import system.dispatcher

  for {
    // write
    _ <- Source(data).runWith(ParquetStreams.toParquetSingleFile.of[Data].write(path.append("data.parquet")))
    // read
    _ <- ParquetStreams.fromParquet.as[Data].read(path).runWith(Sink.foreach(println))
    // finish
    _ <- system.terminate()
  } yield ()

}
