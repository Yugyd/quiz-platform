Content Format
==============

This file contains all the formats that are used to compose content.

# General points

If the content is incorrect, an error will occur when downloading files. Please also read this
manual once.

# Simplified format

The most abbreviated version. Only one question option, a question with four answer options.

## Divide into blocks

Square brackets `[]` separate content into categories and questions.

- `[category]` - A block in which you fill in all the categories.
- `[quest]` - Block in which fill out all the questions.

## Create categories

Full category example.

```
1 // Category ID, each question must have a topic ID
Category A // Category name, text
Description A // Category description, text
https://test.com/test.jpg // Photo category, link to the site with the file or not indicate.
// Optional field
```

## Create questions

Full quest example.

```
Question 1 // Question, text
Answer 1 // Correct answer, text
Answer 2 // Second answer option, text
Answer 3 // Third answer option, text
Answer 4 // Fourth answer option, text
3 // Difficulty level, a number from 1 to 5. Used if sorting mode is enabled in the settings.
1 // Category ID, number, must be the ID of one of the topics above.
1 // Section ID, a number, divides the arcade mode into sections. Any number ascending from 1 to...
// Sections need to be completed with 20 questions each.
```

## Complete example.

```
[category]

1
Category A
Description A

2
Category B
Description B

3
Category B
Description B

[quest]

Question 1
Answer 1
Answer 2
Answer 3
Answer 4
3
1
1
```

# Full json based format

STILL IN WORK. THERE IS NO TEST VERSION OF THE APPLICATION.

Supports many question options.

## General points

[Briefly about the Json format](https://habr.com/ru/articles/554274/)

[Use Json editor for convenience](https://jsonformatter.org/json-editor/)

## Category

- `id` - Category id, each question must have a topic id.
- `ordinal` - Order, number.
- `name` - Category name, text.
- `info` - Category description, text.
- `image` - Photo category, link to the site with the file or not indicate. Optional field, may
  be null.

## I. Question with four possible answers

Question

- `id` - Category id, each question must have a topic id.
- `quest` - Question, text.
- `trueAnswer` - Correct answer, text.
- `answer2` - Second answer option, text.
- `answer3` - Third answer option, text.
- `answer4` - The fourth answer option, text.
- `answer5` - Fifth answer option, text. Optional field, may be null.
- `answer6` - Sixth answer option, text. Optional field, may be null.
- `answer7` - Seventh answer option, text. Optional field, may be null.
- `answer8` - Eighth answer option, text. Optional field, may be null.
- `complexity` - Difficulty level, a number from 1 to 5. Used if the mode is enabled in the settings
  sorting.
- `category` - Category ID, number, must be the ID of one of the topics above.
- `section` - Section ID, number, divides the arcade mode into sections. Any number increasing from
  1
  to... Sections need to be completed with 20 questions each.

Full example.

```
{
  "categories": [
    {
      "id": "1",
      "ordinal": "1",
      "name": "Category A",
      "info": "Info A",
      "image": "https://test.com/test1.jpg"
    },
    {
      "id": "2",
      "ordinal": "2",
      "name": "Category B",
      "info": "Info B",
      "image": "https://test.com/test2.jpg"
    }
  ],
  "quests": [
    {
      "id": "1",
      "quest": "Quest 1",
      "trueAnswer": "Correct answer",
      "answer2": "Answer 2",
      "answer3": "Answer 3",
      "answer4": "Answer 4",
      "answer5": null,
      "answer6": null,
      "answer7": null,
      "answer8": null,
      "complexity": 5,
      "category": 1,
      "section": 1
    },
    {
      "id": "2",
      "quest": "Quest 2",
      "trueAnswer": "Correct answer",
      "answer2": "Answer 2",
      "answer3": "Answer 3",
      "answer4": "Answer 4",
      "answer5": null,
      "answer6": null,
      "answer7": null,
      "answer8": null,
      "complexity": 1,
      "category": 1,
      "section": 2
    }
  ]
}
```
