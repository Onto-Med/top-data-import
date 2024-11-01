package care.smith.top.top_data_import;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileWatcher {

  private final Logger LOGGER = LoggerFactory.getLogger(FileWatcher.class);

  private final WatchService watcher;
  private final Map<WatchKey, Path> keys;

  /** Creates a WatchService and registers the given directory */
  public FileWatcher(Path dir) throws IOException {
    this.watcher = FileSystems.getDefault().newWatchService();
    this.keys = new HashMap<WatchKey, Path>();
    walkAndRegisterDirectories(dir);
  }

  /**
   * Register the given directory with the WatchService; This function will be called by FileVisitor
   */
  private void registerDirectory(Path dir) throws IOException {
    WatchKey key =
        dir.register(
            watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
    keys.put(key, dir);
  }

  /** Register the given directory, and all its subdirectories, with the WatchService. */
  private void walkAndRegisterDirectories(final Path start) throws IOException {
    // register directory and subdirectories
    Files.walkFileTree(
        start,
        new SimpleFileVisitor<Path>() {
          @Override
          public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
              throws IOException {
            registerDirectory(dir);
            return FileVisitResult.CONTINUE;
          }
        });
  }

  /**
   * Process all events for keys queued to the watcher
   *
   * @throws InterruptedException if the watcher was interrupted
   */
  void processEvents() throws InterruptedException {

    WatchKey key = null;
    while ((key = watcher.take()) != null) {

      Path dir = keys.get(key);
      if (dir == null) {
        System.err.println("WatchKey not recognized!!");
        continue;
      }

      for (WatchEvent<?> event : key.pollEvents()) {
        @SuppressWarnings("rawtypes")
        WatchEvent.Kind kind = event.kind();

        // Context for directory entry event is the file name of entry
        @SuppressWarnings("unchecked")
        Path name = ((WatchEvent<Path>) event).context();
        Path child = dir.resolve(name);

        // print out event
        System.out.format("%s: %s\n", event.kind().name(), child);

        // if directory is created, and watching recursively, then register it and its
        // subdirectories
        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
          try {
            if (Files.isDirectory(child)) {
              walkAndRegisterDirectories(child);
            }
          } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
          }
        }
      }

      // reset key and remove from set if directory no longer accessible
      boolean valid = key.reset();
      if (!valid) {
        keys.remove(key);

        // all directories are inaccessible
        if (keys.isEmpty()) {
          break;
        }
      }
    }
  }

  public static void main(String[] args) {
    Path dir = Paths.get("test_files");
    try {
      new FileWatcher(dir).processEvents();
    } catch (InterruptedException | IOException e) {
      e.printStackTrace();
    }
  }
}
