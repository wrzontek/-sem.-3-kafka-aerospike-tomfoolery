package allezon.domain;

import allezon.UserTag;

import java.util.List;

public record PureUserProfileResult(String cookie, List<UserTag> views, List<UserTag> buys) {
}

