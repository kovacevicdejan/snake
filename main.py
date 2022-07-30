import pygame
from sys import exit
from random import randint
from pygame.math import Vector2

class Apple:
    def __init__(self):
        row = randint(0, cell_number - 1)
        col = randint(0, cell_number - 1)
        self.pos = Vector2(row, col)

    def draw(self):
        apple_rect = pygame.Rect(self.pos.x * cell_size, self.pos.y * cell_size, cell_size, cell_size)
        screen.blit(apple_img, apple_rect)


class Snake:
    def __init__(self):
        self.body = [Vector2(5, 9)]
        self.dir = Vector2(0, 0)
        self.increase = False
        self.head = snake_head_right

    def move(self):
        if self.increase:
            self.body.append(self.body[0] + self.dir)
            self.increase = False
        
        new_x = self.body[0].x + self.dir.x
        new_y = self.body[0].y + self.dir.y
        new_head = Vector2(new_x, new_y)
        self.body.insert(0, new_head)
        del self.body[len(self.body) - 1]

    def draw(self):
        snake_color = (235,64,51)
        head_rect = pygame.Rect(self.body[0].x * cell_size, self.body[0].y * cell_size, cell_size, cell_size)
        screen.blit(self.head, head_rect)

        for pos in range(1, len(self.body)):
            snake_rect = pygame.Rect(self.body[pos].x * cell_size, self.body[pos].y * cell_size, cell_size, cell_size)
            pygame.draw.ellipse(screen, snake_color, snake_rect)

    def increase_size(self):
        self.increase = True

    def reset(self):
        self.body = [Vector2(5, 9)]
        self.dir = Vector2(0, 0)
        self.increase = False
        self.head = snake_head_right


class Game:
    def __init__(self):
        self.apple = Apple()
        self.snake = Snake()
        self.is_active = True
        self.eating_sound = pygame.mixer.Sound("sounds/eating_sound.wav")

    def draw_grass(self):
        grass_color = (167,209,61)
        
        for row in range(cell_number):
            for col in range(cell_number):
                if row % 2 == 0 and col % 2 == 0:
                    grass_rect = pygame.Rect(col * cell_size, row * cell_size, cell_size, cell_size)
                    pygame.draw.rect(screen, grass_color, grass_rect)
                elif row % 2 == 1 and col % 2 == 1:
                    grass_rect = pygame.Rect(col * cell_size, row * cell_size, cell_size, cell_size)
                    pygame.draw.rect(screen, grass_color, grass_rect)

    def draw_score(self):
        score_text = 'Score: ' + str(len(self.snake.body) - 1)
        score_surf = score_font.render(score_text, True, (56, 
        74, 12))
        score_rect = score_surf.get_rect(topleft = (600, 755))
        screen.blit(score_surf, score_rect)

    def draw_elements(self):
        self.draw_grass()
        self.apple.draw()
        self.snake.draw()
        self.draw_score()

    def game_over(self):
        self.is_active = False

    def check_collision(self):
        if self.apple.pos == self.snake.body[0]:
            del self.apple
            self.apple = Apple()
            self.snake.increase_size()
            self.eating_sound.play()

    def check_crash(self):
        if not 0 <= self.snake.body[0].x < cell_number:
            self.game_over()

        if not 0 <= self.snake.body[0].y < cell_number:
            self.game_over()

        for block in self.snake.body[1:]:
            if block == self.snake.body[0]:
                self.game_over()

    def update(self):
        self.snake.move()
        self.check_collision()
        self.check_crash()


pygame.init()
cell_size = 40
cell_number = 20
screen = pygame.display.set_mode((cell_number * cell_size,cell_number * cell_size))

apple_img = pygame.image.load("images/apple.png")
snake_head_left = pygame.image.load("images/snake_head_left.png")
snake_head_right = pygame.image.load("images/snake_head_right.png")
snake_head_up = pygame.image.load("images/snake_head_up.png")
snake_head_down = pygame.image.load("images/snake_head_down.png")

game_over = pygame.image.load("images/game_over.png")
game_over = pygame.transform.rotozoom(game_over, 0, 0.5)
game_over_rect = game_over.get_rect(topleft = (270, 150))

score_font = pygame.font.Font("fonts/PoetsenOne-Regular.ttf", 40)
pygame.display.set_caption('Snake')
clock = pygame.time.Clock()

SCREEN_UPDATE = pygame.USEREVENT
pygame.time.set_timer(SCREEN_UPDATE, 150)
game = Game()

while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            exit()

        if game.is_active:
            if event.type == SCREEN_UPDATE:
                game.update()

            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_LEFT:
                    if game.snake.dir.x != 1:
                        game.snake.dir = Vector2(-1, 0)
                        game.snake.head = snake_head_left
                elif event.key == pygame.K_RIGHT:
                    if game.snake.dir.x != -1:
                        game.snake.dir = Vector2(1, 0)
                        game.snake.head = snake_head_right
                elif event.key == pygame.K_UP:
                    if game.snake.dir.y != 1:
                        game.snake.dir = Vector2(0, -1)
                        game.snake.head = snake_head_up
                elif event.key == pygame.K_DOWN:
                    if game.snake.dir.y != -1:
                        game.snake.dir = Vector2(0, 1)
                        game.snake.head = snake_head_down
        else:
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_SPACE:
                    game.snake.reset()
                    game.is_active = True

    if game.is_active:
        screen.fill((175,215,70))
        game.draw_elements()
    else:
        screen.fill((167,209,61))
        screen.blit(game_over, game_over_rect)

        score_message = score_font.render(f'Your score: {len(game.snake.body) - 1}', False, (56, 74, 12))
        score_message_rect = score_message.get_rect(center = (400, 450))
        screen.blit(score_message, score_message_rect)

        message = score_font.render('Press space for new game', False, (56, 74, 12))
        message_rect = message.get_rect(center = (400, 500))
        screen.blit(message, message_rect)
            
    pygame.display.update()
    clock.tick(60)
