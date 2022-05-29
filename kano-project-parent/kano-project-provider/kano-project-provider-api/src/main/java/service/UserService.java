package service;

import com.kano.project.common.model.Result;
import dto.UserReqDTO;

/**
 * @author kano
 */
public interface UserService {

    void queryStudent();

    Result<Boolean> insetUser(UserReqDTO reqDTO);
}
