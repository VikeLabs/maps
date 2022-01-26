local tables = {}

tables.point = osm2pgsql.define_node_table('uvic_point', {
    { column = 'name', type = 'text', not_null = true },
    { column = 'abbr_name', type = 'text' },
    { column = 'geom', type = 'point', projection = 'latlon'  },
})

function osm2pgsql.process_node(object)
    if object.tags.name == nil then
        return
    else
        tables.point:add_row({
            name = object.tags.name,
            abbr_name = object.tags.abbr_name,
            geom = { create = 'point' }
        })
    end
end

tables.way = osm2pgsql.define_way_table('uvic_way', {
    { column = 'name', type = 'text', not_null = true },
    { column = 'abbr_name', type = 'text' },
    { column = 'geom', type = 'linestring', projection = 'latlon'  },
})

function osm2pgsql.process_way(object)
    if object.tags.name == nil then
        return
    else
        tables.way:add_row({
            name = object.tags.name,
            abbr_name = object.tags.abbr_name,
            geom = { create = 'line' }
        })
    end
end

tables.area = osm2pgsql.define_area_table('uvic_area', {
    { column = 'name', type = 'text', not_null = true },
    { column = 'abbr_name', type = 'text' },
    { column = 'geom', type = 'geometry', projection = 'latlon' },
})

function osm2pgsql.process_relation(object)
    if object.tags.name == nil then
        return
    else
        if object.tags.type == 'multipolygon' or object.tags.type == 'boundary' then
            tables.area:add_row({
                name = object.tags.name,
                abbr_name = object.tags.abbr_name,
                geom = { create = 'area' }
            })
        end
    end
end
