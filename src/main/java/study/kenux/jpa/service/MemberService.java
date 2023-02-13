package study.kenux.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.kenux.jpa.repository.MemberRepository;
import study.kenux.jpa.repository.TeamRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
}
