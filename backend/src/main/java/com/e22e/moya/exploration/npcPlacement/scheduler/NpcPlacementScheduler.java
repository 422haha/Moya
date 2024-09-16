package com.e22e.moya.exploration.npcPlacement.scheduler;

import com.e22e.moya.exploration.npcPlacement.service.NpcPlacementService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NpcPlacementScheduler {
    private final NpcPlacementService npcPlacementService;

    public NpcPlacementScheduler(NpcPlacementService npcPlacementService) {
        this.npcPlacementService = npcPlacementService;
    }

    @Scheduled(cron = "0 0 3 * * ?") // 매일 새벽 3시에 실행
    public void scheduleNpcPlacementUpdate() {
        npcPlacementService.addNpcPos();
    }
}
