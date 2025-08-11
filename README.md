Download on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/garden-cloche-backport)

# CraftTweaker Guide

There are a few plant plant handlers this mod comes with, the difference between them is how the plant growth animation is rendered.
They should work for most plants if they extend vanilla crop functionality. In my experience the majority of modded plants should work.

## Adding "Default" Crops

**Parameters:** String id, IItemStack seed, IItemStack soil, Array of IItemStacks drops, IItemStack renderBlock, int maxGrowthMeta

`mods.immersiveengineering.GardenCloche.addDefaultCrop(String, IItemStack, IItemStack, [IItemStack], IItemStack, int);`

**Example:**

`mods.immersiveengineering.GardenCloche.addDefaultCrop("artichoke",<witchery:seedsartichoke>,<minecraft:water_bucket>,[<witchery:ingredient:69>*2],<witchery:artichoke>,4);`

This adds a handler for witchery's artichoke plant. The water bucket is set to be the soil since it grows on water and it outputs 2 artichokes per growth cycle.
Note that 4 is max block meta of a fully grown artichoke plant this is needed so the animation is rendered correctly. Not all plants have the same max meta you can use mods like WAILA to help you determine it.
`<witchery:artichoke>` referes to an artichoke **block** not an item, you must specify it for rendering to work. Most crops that you plant on farmland are probably "default" crops

## Adding Stem Crops

**Parameters:** String id, IItemStack seed, IItemStack soil, Array of IItemStacks drops, IItemStack stemBlock, IItemStack productBlock

`mods.immersiveengineering.GardenCloche.addStemCrop(String, IItemStack, IItemStack, [IItemStack], IItemStack, IItemStack);`

**Example:**

`mods.immersiveengineering.GardenCloche.addStemCrop("redlon",<thaumicbases:redlonSeeds>,<minecraft:dirt>,[<minecraft:redstone_block>],<thaumicbases:redlonStem>,<minecraft:redstone_block>);`

Just like in the previous example most of the parameters are the same. The only difference is that instead of specifying an int for max meta you provide a block that growth on a stem(melon block, pumpkin block, etc.) 
In this case this a redlon plant from ThaumicBases which grows redstone blocks. 

## Adding Reed Crops

**Parameters:** String id, IItemStack seed, IItemStack soil, Array of IItemStacks drops, IItemStack reedBlock

`mods.immersiveengineering.GardenCloche.addReedCrop(String, IItemStack, IItemStack, [IItemStack], IItemStack);`

**Example:**

`mods.immersiveengineering.GardenCloche.addReedCrop("infernalReed",<netherlicious:InfernalReedItem>,<minecraft:soul_sand>,[<netherlicious:InfernalReedItem>*2],<netherlicious:InfernalReed>);`

When adding reed crop handlers you just need to specify the reed block itself, no additional parameters for rendering are needed.

## Adding Cactus Crops

**Parameters:** String id, IItemStack seed, IItemStack soil, Array of IItemStacks drops, IItemStack cactusBlock

`mods.immersiveengineering.GardenCloche.addCactus(String, IItemStack, IItemStack, [IItemStack], IItemStack);`

**Example:**

`mods.immersiveengineering.GardenCloche.addCactus("rainbowCactus",<thaumicbases:rainbowCactus>,<minecraft:sand>,[<thaumicbases:rainbowCactus>],<thaumicbases:rainbowCactus>);`

When adding cactus crop handlers you just need to specify the cactus block itself, no additional parameters for rendering are needed.

## Adding Crops without render

**Parameters:** String id, IItemStack seed, IItemStack soil, Array of IItemStacks drops

`mods.immersiveengineering.GardenCloche.addRenderlessCrop(String, IItemStack, IItemStack, [IItemStack]);`

**Example:**

`mods.immersiveengineering.GardenCloche.addRenderlessCrop("rainbowCactus",<thaumicbases:rainbowCactus>,<minecraft:sand>,[<thaumicbases:rainbowCactus>]);`

There may be cases where a plant doesn't match or is able to work with provided plant handlers. If thats the case you can register a plant without rendering, it will just output the drops you specify but will not have a growing animation

## Registering Soil Textures

**Parameters:** String id, IItemStack soil, String pathToTexture

`mods.immersiveengineering.GardenCloche.addSoilTextureMapping(String, IItemStack, String)`

**Example:**

`mods.immersiveengineering.GardenCloche.addSoilTextureMapping("soulFarmland",<netherlicious:SoulSoil>,"netherlicious:textures/blocks/soul_farmland_wet.png");`

In order for the "soil" item to be rendered inside of the garden cloche you must register the item to its texture. The texture doesn't have to match the item, the water bucker for example is already registered to the water texture, dirt is registered to farmland texture, etc...

## Removing Plant Handlers

**Parameters:** String id

`mods.immersiveengineering.GardenCloche.removeHandler(String)`

**Example:**

`mods.immersiveengineering.GardenCloche.removeHandler("wheatHandler");`

If you wanted to remove existing plant handlers that are registered by default by this mod you can do so with this command. IDs for handlers registered by default are: wheatHandler, potatoHandler, carrotHandler, netherwartHandler, hempHandler, melonHandler, pumpkinHandler, cactusHandler, caneHandler. 

## Registering Fertilizers

**Parameters:** IItemStack fertilizer, float boost

`mods.immersiveengineering.GardenCloche.addFertilizerHandler(String)`

**Example:**

`mods.immersiveengineering.GardenCloche.addFertilizerHandler(<minecraft:bone>, 0.5f);`

You can register items as fertilizers and specify how much they should boost growth speed, 0.5 is the same as 50% speed boost.





