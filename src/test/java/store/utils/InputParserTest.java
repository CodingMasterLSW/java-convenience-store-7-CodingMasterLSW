package store.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.dto.ParseItemDto;

public class InputParserTest {

    @DisplayName("사용자의 입력이 주어졌을 때, 구매 리스트로 변환한다.")
    @Test
    void input_parser_test() {
        String userInput = "[사이다-2],[감자칩-1]";

        List<ParseItemDto> parseItemDtos = InputParser.parseInputToItems(userInput);

        assertThat(parseItemDtos)
                .containsExactly(
                        ParseItemDto.of("사이다", 2),
                        ParseItemDto.of("감자칩", 1));
    }
}
