package security.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity , Integer> {
    Optional<MemberEntity>findBymid(String mid); //memberEntity 중에 mid 같은게 있는지 찾기

}
