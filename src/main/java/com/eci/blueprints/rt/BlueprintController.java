package com.eci.blueprints.rt;

import com.eci.blueprints.rt.dto.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class BlueprintController {

  private final SimpMessagingTemplate template;

  private final Map<String, List<Point>> store = new ConcurrentHashMap<>();
  private static String keyOf(String a, String n) { return a + "/" + n; }


  public BlueprintController(SimpMessagingTemplate template) {
    this.template = template;
  }


  @MessageMapping("/draw")
  public void onDraw(@Payload DrawEvent msg) {
    if (msg == null || msg.point() == null || msg.author() == null || msg.name() == null) return;

    String key = keyOf(msg.author(), msg.name());
    store.computeIfAbsent(key, k -> new ArrayList<>()).add(msg.point());

    List<Point> points = store.get(key);
    String topic = String.format("/topic/blueprints.%s.%s", msg.author(), msg.name());

    template.convertAndSend(topic, new BlueprintUpdate(msg.author(), msg.name(), points));
  }


  @ResponseBody
  @GetMapping("/api/blueprints/{author}/{name}")
  public BlueprintUpdate get(@PathVariable String author, @PathVariable String name) {
    return new BlueprintUpdate(author, name, List.of(new Point(10,10), new Point(40,50)));
  }
}
