package com.loababa.api.auth.infra.persistance.adapter;

import com.loababa.api.auth.domain.impl.model.OAuthUser;
import com.loababa.api.auth.infra.persistance.repository.OAuthUserJpaRepository;
import com.loababa.api.common.MockTestBase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class OAuthUserJpaRepositoryAdapterTest extends MockTestBase {

    @InjectMocks
    private OAuthUserJpaRepositoryAdapter oAuthUserJpaRepositoryAdapter;
    @Mock
    private OAuthUserJpaRepository oAuthUserJpaRepository;

    @ParameterizedTest
    @MethodSource("com.loababa.api.auth.domain.impl.model.OAuthUserFixtures#newOAuthUsers")
    void OAuthUser를_저장할_수_있다(OAuthUser oAuthUser) {
        // given

        // when
        oAuthUserJpaRepositoryAdapter.save(oAuthUser);

        // then
        then(oAuthUserJpaRepository).should(times(1)).save(
                argThat(entity -> Objects.equals(entity.getOAuthId(), oAuthUser.oAuthUserId()) &&
                        Objects.equals(entity.getOAuthPlatform(), oAuthUser.oAuthPlatform()))
        );

    }


}
